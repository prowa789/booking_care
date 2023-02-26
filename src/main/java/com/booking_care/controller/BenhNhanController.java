package com.booking_care.controller;

import com.booking_care.config.VNPayConfig;
import com.booking_care.constant.Constants;
import com.booking_care.constant.ewewwewe.Status;
import com.booking_care.model.*;
import com.booking_care.model.request.*;
import com.booking_care.repository.*;
import com.booking_care.security.CustomUserDetails;

import com.booking_care.utils.DateUtils;
import com.booking_care.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class BenhNhanController {

    @Autowired
    BenhNhanRepository benhNhanRepo;

    @Autowired
    BacSyRepository bacSyRepo;

    @Autowired
    LichKhamRepository lichKhamRepo;

    @Autowired
    TaiKhoanRepository taiKhoanRepo;

    @Autowired
    ChuyenKhoaRepository chuyenKhoaRepo;

    @Autowired
    VaiTroRepository vaiTroRepo;

    DateFormat formatDate2 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping(value = {"","trang-chu"})
    public String home(Model model, @AuthenticationPrincipal CustomUserDetails taiKhoan) {
        if(taiKhoan == null) {
            return "gioi-thieu";
        }
        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        model.addAttribute("benhNhan",benhNhan);
        return "gioi-thieu";
    }


    @GetMapping ("dang-ki")
    public String viewDangKi() {
        return "dang-ki";
    }

    @GetMapping ("dang-nhap")
    public String viewDangNhap() {
        return "dang-nhap";
    }

    @ModelAttribute
    SignUpRequest signUpRequest(){
        return new SignUpRequest();
    }

    @ModelAttribute
    TaiKhoan taiKhoan(){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof CustomUserDetails && ((CustomUserDetails) user).hasRole("BENH_NHAN") ){
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getTaiKhoan();
        }
        return null;
    }

    @PostMapping("dang-ki")
    @Transactional
    public String dangKi(@ModelAttribute @Valid SignUpRequest signUpRequest,
                         Errors errors,Model model, RedirectAttributes redirectAttributes) throws ParseException {
        System.out.println(signUpRequest.toString());
        if(errors.hasErrors()) {
            return "dang-ki";
        }
        if(taiKhoanRepo.findByUsername(signUpRequest.getUsername()) != null) {
            model.addAttribute("loi","Tài khoản đã tồn tại");
            return "dang-ki";
        }
        // tao tai khoan cho benh nhan
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setUsername(signUpRequest.getUsername());
        taiKhoan.setPassword(signUpRequest.getPassword());
        taiKhoan.setVaiTro(vaiTroRepo.findById(Constants.VaiTro.ROLE_BENH_NHAN).get());
        taiKhoanRepo.save(taiKhoan);
        // luu thong tin benh nhan
        BenhNhan benhNhan = new BenhNhan();
        benhNhan.setHoTen(signUpRequest.getHoTen());
        benhNhan.setEmail(signUpRequest.getEmail());
        benhNhan.setNgaySinh(format.parse(signUpRequest.getNgaySinh()));
        benhNhan.setSdt(signUpRequest.getSdt());
        benhNhan.setTaiKhoan(taiKhoan);
        System.out.println(benhNhan);
        System.out.println(taiKhoan);
        benhNhanRepo.save(benhNhan);
        redirectAttributes.addFlashAttribute("ok", "Tạo tài khoản mới thành công");
        return "redirect:/dang-ki";
    }

    @GetMapping("dat-lich")
    @PreAuthorize("hasAuthority('BENH_NHAN')")
    public String datLichKhambenh(Model model, @AuthenticationPrincipal CustomUserDetails taiKhoan) {
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }
        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        List<ChuyenKhoa> chuyenKhoaList = chuyenKhoaRepo.findAll();
        model.addAttribute("benhNhan",benhNhan);
        model.addAttribute("chuyenKhoaList",chuyenKhoaList);
        Map<Integer,String> khungGioKham = new HashMap<>();
        khungGioKham.put(1,"Từ 8h30 đến 9h30");
        khungGioKham.put(2,"Từ 10h00 đến 11h00");
        khungGioKham.put(3,"Từ 14h00 đến 15h00");
        khungGioKham.put(4,"Từ 15h30 đến 16h30");
        model.addAttribute("khungGioKham",khungGioKham);

        return "booking";
    }

    @PostMapping("dat-lich")
    public String dangKiKhamBenh(@AuthenticationPrincipal CustomUserDetails taiKhoan,
                                 @ModelAttribute BookingRequest bookingRequest,
                                 RedirectAttributes redirectAttributes) throws ParseException {
        System.out.println(bookingRequest.toString());
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }
//        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        if(!bookingRequest.isValid()) {
            redirectAttributes.addFlashAttribute("loi", "Booking Request không hợp lệ");
            return "redirect:/dat-lich";
        }
        List<LichKham> lichKhamList =  lichKhamRepo.findByNgayKhamAndBacSyIdAndKhungGioKham(
                format.parse(bookingRequest.getNgayKham()),bookingRequest.getBacSyId(),bookingRequest.getKhungGioKham());
        if(!lichKhamList.isEmpty()) {
            redirectAttributes.addFlashAttribute("loi", "Thời gian này đã có người đặt");
            return "redirect:/dat-lich";
        }

//        if(lichKhamRepo.existsByBenhNhan(benhNhan.getId()).isEmpty()) {
//            redirectAttributes.addFlashAttribute("loi", "Mỗi bệnh nhân chỉ đặt tối đa 1 lịch khám: chưa xử lý");
//            return "redirect:/dat-lich";
//        }

        LichKham lichKham = new LichKham();
        lichKham.setBenhNhan(benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan()));
        lichKham.setBacSy(bacSyRepo.findById(bookingRequest.getBacSyId()).get());
        lichKham.setStatus(Status.CHO_XU_LY);
        lichKham.setKhungGioKham(bookingRequest.getKhungGioKham());
        lichKham.setChuyenKhoaId(bookingRequest.getChuyenKhoaId());
        lichKham.setMoTaTrieuChung(bookingRequest.getMoTaTrieuChung());
        lichKham.setNgayKham(format.parse(bookingRequest.getNgayKham()));
        lichKham.setTienKham(lichKham.getBacSy().getTienKham());

        lichKhamRepo.save(lichKham);
        redirectAttributes.addFlashAttribute("ok","Đặt lịch thành công");
        return "redirect:/dat-lich";
    }

    @GetMapping("huyLichKham/{id}")
    public String huyLichKham(@AuthenticationPrincipal CustomUserDetails taiKhoan,@PathVariable Integer id,
                              RedirectAttributes redirectAttributes){
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }
        LichKham lichKham = lichKhamRepo.getById(id);
        lichKham.setStatus(Status.DA_HUY);
        lichKhamRepo.save(lichKham);
        redirectAttributes.addFlashAttribute("ok","Hủy lịch khám thành công");
        return "redirect:/lich-kham-benh?status=2";
    }

    @GetMapping("lich-kham-benh")
    @PreAuthorize("hasAuthority('BENH_NHAN')")
    public String lichKhamBenh(Model model,
                               @AuthenticationPrincipal CustomUserDetails taiKhoan,
                               @RequestParam(value = "page", required = true, defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "status", required = false) Integer status) {
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }
        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        List<LichKham> lichKhamList = null;
        if(status!=null) {
            if(status == 0) {
                lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhanByStatus(benhNhan.getId() ,Status.CHO_XU_LY, PageRequest.of(page-1,pageSize));
            }
            if(status==1){
                lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhanByStatus(benhNhan.getId() ,Status.DA_XAC_NHAN, PageRequest.of(page-1,pageSize));
            }
            if(status==2){
                lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhanByStatus(benhNhan.getId() ,Status.DA_HUY, PageRequest.of(page-1,pageSize));
            }
            if(status==3){
                lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhanByStatus(benhNhan.getId() ,Status.DA_KHAM, PageRequest.of(page-1,pageSize));
            }
        }else {
            lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhan(benhNhan.getId(), PageRequest.of(page-1,pageSize));

        }

        model.addAttribute("lichKhamList", lichKhamList);

        return "lich-kham-benh";
    }

    @GetMapping("profile")
    @PreAuthorize("hasAuthority('BENH_NHAN')")
    public String viewProfile(Model model,@AuthenticationPrincipal CustomUserDetails taiKhoan){
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }
        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        System.out.println(benhNhan);
        model.addAttribute("benhNhan",benhNhan);
        return "profile-benh-nhan";
    }
    @PostMapping("profile")
    @PreAuthorize("hasAuthority('BENH_NHAN')")
    @Transactional
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails taiKhoan,
                                @ModelAttribute(name = "benhNhan") BenhNhanRequest benhNhanRequest,
                                @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, ParseException {
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }

        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());

        if(file.isEmpty()) {
            benhNhanRepo.updateProfileBenhNhanKoUploadFile(benhNhanRequest.getDiaChi(),benhNhanRequest.getHoTen(),
                    format.parse(benhNhanRequest.getNgaySinh()),benhNhanRequest.getSdt(),benhNhanRequest.getEmail(),benhNhan.getId());
            redirectAttributes.addFlashAttribute("ok","Update thành công");

            return "redirect:/profile";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "benhnhan-photos/" + benhNhan.getId();
        System.out.println(benhNhanRequest.toString());
        benhNhanRepo.updateProfileBenhNhan(benhNhanRequest.getDiaChi(),benhNhanRequest.getHoTen(),
                format.parse(benhNhanRequest.getNgaySinh()),benhNhanRequest.getSdt(),benhNhanRequest.getEmail(),
                fileName,benhNhan.getId());

        FileUploadUtil.saveFile(uploadDir, fileName, file);

        redirectAttributes.addFlashAttribute("ok","Update thành công");

        return "redirect:/profile";
    }
    @GetMapping("/doi-mat-khau")
    @PreAuthorize("hasAuthority('BENH_NHAN')")
    public String viewDoiMatKhau(Model model,@AuthenticationPrincipal CustomUserDetails taiKhoan){
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }
        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());

        model.addAttribute("benhNhan",benhNhan);
        model.addAttribute("doiMatKhauRequest", new DoiMatKhauRequest());
        return "doimatkhau-benh-nhan";
    }
    @PostMapping("/doi-mat-khau")
    @PreAuthorize("hasAuthority('BENH_NHAN')")
    @Transactional
    public String updateMatKhau(@ModelAttribute @Valid DoiMatKhauRequest doiMatKhauRequest,
                                BindingResult bindingResult,
                                Model model,@AuthenticationPrincipal CustomUserDetails taiKhoan,
                                RedirectAttributes redirectAttributes,Errors errors){

        System.out.println(doiMatKhauRequest.toString());
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }

        //validate đầu vào
        if(errors.hasErrors()) {
            return "doimatkhau-benh-nhan";
        }

        // check mật khẩu hiện tại
        if(!doiMatKhauRequest.getMatKhauHienTai().equals(taiKhoan.getPassword())) {
            redirectAttributes.addFlashAttribute("loiMatKhau1","Mật khẩu hiện tại sai mật khẩu cũ");
            return "redirect:/doi-mat-khau";
        }
        // update mật khẩu mới
        taiKhoanRepo.updateMatKhau(doiMatKhauRequest.getMatKhauMoi(),taiKhoan.getTaiKhoan().getUsername());
        model.addAttribute("ok","Update mật khẩu thành công");
        return "doimatkhau-benh-nhan";
    }

    @GetMapping("/danh-sach-bac-sy")
    public String getAllBacSy(Model model){
        List<BacSy> bacSyList = bacSyRepo.findAll();
        model.addAttribute("bacSyList", bacSyList);
        return "list-doctor";
    }
    @GetMapping("/bac-sy/{id}")
    public String getBacSy(@PathVariable Integer id ,Model model){
        BacSy bacSy = bacSyRepo.findById(id).get();
        model.addAttribute("bacSy", bacSy);
        return "detail-doctor";
    }
    @PostMapping("/thanhToan")
    public String thanhToanMoMo(HttpServletRequest request,
                                @RequestParam("payment_method") String paymentMethod,
                                @RequestParam("tienKham") Integer tienKham,
                                @RequestParam("idLichKham") Integer idLichKham)
            throws JSONException, UnsupportedEncodingException {
        System.out.println(paymentMethod + tienKham + idLichKham);
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = "1";
        String orderType = "fashion";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(tienKham*100));
        vnp_Params.put("vnp_CurrCode", "VND");
//        vnp_Params.put("idLichKham", String.valueOf(idLichKham));
        vnp_Params.put("vnp_BankCode", paymentMethod);

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_Returnurl+"?idLichKham=" + idLichKham);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 10);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        //Add Params of 2.0.1 Version
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        //Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return "redirect:"+paymentUrl;

    }
    @GetMapping("/thanh-toan")
    @PreAuthorize("hasAuthority('BENH_NHAN')")
    public String thanhToan(Model model,@AuthenticationPrincipal CustomUserDetails taiKhoan,
                            @RequestParam("vnp_ResponseCode") String responseCode,
                            @RequestParam("idLichKham") Integer idLichKham){
        if(taiKhoan==null || !taiKhoan.hasRole("BENH_NHAN")) {
            return "redirect:/dang-nhap";
        }
        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        if(responseCode.equals("00")) {
            //Luu thong tin dang ký thanh cong
            LichKham lichKham = lichKhamRepo.findById(idLichKham).get();
            lichKham.setPaid(true);
            lichKhamRepo.save(lichKham);
            model.addAttribute("msg","Thanh toán thành công cho lịch khám " + idLichKham);
            model.addAttribute("lichKham", lichKham);
            return "thanhtoan";
        }else {
            //xử lý thông báo thanh toán thất bại
            LichKham lichKham = lichKhamRepo.findById(idLichKham).get();
            lichKhamRepo.save(lichKham);
            model.addAttribute("msg","Thanh toán thất bại cho lịch khám " + idLichKham);
            model.addAttribute("lichKham", lichKham);
            return "thanhtoan";
        }
    }
}
