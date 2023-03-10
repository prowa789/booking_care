package com.booking_care.controller;

import com.booking_care.constant.ewewwewe.Status;
import com.booking_care.model.*;
import com.booking_care.model.request.BacSyRequest;
import com.booking_care.model.request.DoiMatKhauRequest;
import com.booking_care.repository.BacSyRepository;
import com.booking_care.repository.ChuyenKhoaRepository;
import com.booking_care.repository.LichKhamRepository;
import com.booking_care.repository.TaiKhoanRepository;
import com.booking_care.security.CustomUserDetails;
import com.booking_care.service.EmailSenderService;
import com.booking_care.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BacSyController {
    @Autowired
    public JavaMailSender mailSender;
    @Autowired
    BacSyRepository bacSyRepo;
    @Autowired
    LichKhamRepository lichKhamRepo;
    @Autowired
    ChuyenKhoaRepository chuyenKhoaRepo;
    @Autowired
    TaiKhoanRepository taiKhoanRepo;
    @Autowired
    EmailSenderService emailSenderService;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @ModelAttribute
    TaiKhoan taiKhoan() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof CustomUserDetails && ((CustomUserDetails) user).hasRole("BENH_NHAN")) {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getTaiKhoan();
        }
        return null;
    }

    @GetMapping("/danh-sach-bac-sy")
    public String getAllBacSy(Model model, @RequestParam(value = "chuyenKhoaId", required = false) Integer ckId, @RequestParam(value = "bsName", required = false) String bsName,
                              @RequestParam(value = "page", required = true, defaultValue = "1") Integer page,
                              @RequestParam(value = "pageSize", required = true, defaultValue = "5") Integer pageSize,
                              HttpServletRequest request) {
        Page<BacSy> bacSyPage = null;
        String query = request.getQueryString();
        if ((query != null && query.length() > 6)) {
            if (query.contains("page")) {
                query = query.substring(query.length() - 7);
            }

            model.addAttribute("query", query);
            model.addAttribute("issearch", true);
        } else {
            model.addAttribute("issearch", false);
        }

        if ((bsName == null && ckId == null)) {
            bacSyPage = bacSyRepo.findAll(PageRequest.of(page - 1, pageSize));
        } else if (ckId != null && ckId == 0) {
            model.addAttribute("ckId", ckId);
            model.addAttribute("bsName", bsName);
            bacSyPage = bacSyRepo.getAllByName(bsName, PageRequest.of(page - 1, pageSize));
        } else if (ckId != null && ckId > 0) {
            bacSyPage = bacSyRepo.getAllByChuyenKhoaAndName(ckId, bsName, PageRequest.of(page - 1, pageSize));
            model.addAttribute("ckId", ckId);
            model.addAttribute("bsName", bsName);
        }

        model.addAttribute("chuyenKhoaList", chuyenKhoaRepo.findAll());
        model.addAttribute("bacSyList", bacSyPage.getContent());
        model.addAttribute("bacSyPage", bacSyPage);
        int totalPages = bacSyPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("totalPage", bacSyPage.getTotalPages());
            System.out.println(bacSyPage.getSize());
        }
        return "list-doctor";
    }

    @GetMapping("/bac-sy/{id}")
    public String getBacSy(@PathVariable Integer id, Model model) {
        BacSy bacSy = bacSyRepo.findById(id).get();
        model.addAttribute("bacSy", bacSy);
        return "detail-doctor";
    }

    @GetMapping("/bacsy/login")
    public String viewBacSyLoginPage() {
        return "bacsy/bacsy_login";
    }

    @GetMapping("/bacsy")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String homeBacSy2() {
        return "bacsy/bacsy_home";
    }

    @GetMapping("/bacsy/home")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String homeBacSy() {
        return "bacsy/bacsy_home";
    }


    @GetMapping("bacsy/danhSachLichKham")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String getAllLichKham(@AuthenticationPrincipal CustomUserDetails taiKhoan, @RequestParam(value = "page", required = true, defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                                 Model model) {
        if (taiKhoan == null || !taiKhoan.hasRole("BAC_SY")) {
            return "redirect:/bacsy/login";
        }
        BacSy bacSy = bacSyRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        List<LichKham> lichKhamList = lichKhamRepo.getLichKhamByBacSyId(bacSy.getId(), PageRequest.of(page - 1, pageSize));
//        System.out.println(lichKhamList);
        model.addAttribute("lichKhamList", lichKhamList);
        return "bacsy/bacsy_xem_lich_kham";
    }

    @GetMapping("bacsy/xemChiTietLichKham")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String xemChiTietLichKham(@AuthenticationPrincipal CustomUserDetails taiKhoan, Integer pageSize,
                                     Model model, @RequestParam("id") Integer id) {
        if (taiKhoan == null || !taiKhoan.hasRole("BAC_SY")) {
            return "redirect:/bacsy/login";
        }
        LichKham lichKham = lichKhamRepo.findById(id).get();
        model.addAttribute("lichKham", lichKham);
        return "bacsy/bacsy_xem_chi_tiet_lich_kham";
    }

    @PostMapping("bacsy/xacNhan")
    @PreAuthorize("hasAuthority('BAC_SY')")
    @Transactional
    public String xacNhanLichKham(@AuthenticationPrincipal CustomUserDetails taiKhoan, Integer pageSize,
                                  Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        if (taiKhoan == null || !taiKhoan.hasRole("BAC_SY")) {
            return "redirect:/bacsy/login";
        }
        LichKham lichKham = null;
        try {
            lichKham = lichKhamRepo.findById(id).get();
            lichKham.setStatus(Status.DA_XAC_NHAN);

            // send email
            Email email = new Email();
            email.setTo(lichKham.getBenhNhan().getEmail());
            email.setFrom(new InternetAddress("asacnuoc@gmail.com", "Booking Care System"));
            email.setSubject("Thông tin chi tiết lịch khám");
            email.setTemplate("template-email.html");
            Map<String, Object> properties = new HashMap<>();
            properties.put("lichKham", lichKham);
            email.setProperties(properties);
            emailSenderService.sendHtmlMessage(email);

        } catch (SendFailedException e) {
            redirectAttributes.addFlashAttribute("ok", "Gửi email thất bại!");
            lichKham.setStatus(Status.CHO_XU_LY);
            return "redirect:/bacsy/xemChiTietLichKham?id=" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("ok", "Thất bại!");
            lichKham.setStatus(Status.CHO_XU_LY);
            return "redirect:/bacsy/xemChiTietLichKham?id=" + id;
        }
        redirectAttributes.addFlashAttribute("ok", "Xác nhận và gửi email thành công!");
        if (lichKham != null)
            lichKhamRepo.save(lichKham);
        return "redirect:/bacsy/xemChiTietLichKham?id=" + id;
    }

    @GetMapping("/bacsy/profile")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String viewBacSy(Model model, @AuthenticationPrincipal CustomUserDetails taiKhoan) {
        if (taiKhoan == null || !taiKhoan.hasRole("BAC_SY")) {
            return "redirect:/bacsy/login";
        }
        BacSy bacSy = bacSyRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        List<ChuyenKhoa> chuyenKhoaList = chuyenKhoaRepo.findAll();
        model.addAttribute("bacSy", bacSy);
        model.addAttribute("chuyenKhoaList", chuyenKhoaList);
        return "bacsy/bacsy_profile";
    }

    @Transactional
    @PostMapping("/bacsy/profile")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String updateBacSy(RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails taiKhoan,
                              @ModelAttribute(name = "bacSy") @Valid BacSyRequest bacSyRequest, @RequestParam("image") MultipartFile file,
                              Errors errors) {
        if (errors.hasErrors()) {
            return "bacsy/profile";
        }
        try {
            BacSy bacSy = bacSyRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());

            if (file.isEmpty()) {
                bacSyRepo.updateThongTinBacSy(format.parse(bacSyRequest.getNgaySinh()), bacSyRequest.getHoTen(),
                        bacSyRequest.getChucVu(), bacSyRequest.getChuyenKhoaId(),
                        bacSyRequest.getSdt(), bacSyRequest.getEmail(), bacSyRequest.getChungChi(), bacSyRequest.getKinhNghiem(),
                        bacSyRequest.getLinhVucChuyenSau(),bacSyRequest.getTienKham(),bacSyRequest.getNoiKham(), bacSyRequest.getId());
                redirectAttributes.addFlashAttribute("ok", "Update thành công");
                return "redirect:/bacsy/profile";
            }
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            bacSyRepo.updateThongTinBacSyAndUploadFile(format.parse(bacSyRequest.getNgaySinh()), bacSyRequest.getHoTen(),
                    bacSyRequest.getChucVu(), bacSyRequest.getChuyenKhoaId(),
                    bacSyRequest.getSdt(), bacSyRequest.getEmail(), fileName, bacSyRequest.getChungChi(),
                    bacSyRequest.getKinhNghiem(), bacSyRequest.getLinhVucChuyenSau(),bacSyRequest.getTienKham(),bacSyRequest.getNoiKham(), bacSyRequest.getId());
            String uploadDir = "bacsy-photos/" + bacSy.getId();

            FileUploadUtil.saveFile(uploadDir, fileName, file);

            redirectAttributes.addFlashAttribute("ok", "Update thành công");
            return "redirect:/bacsy/profile";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/bacsy/profile";
    }

    @GetMapping("bacsy/doiMatKhau")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String viewDoiMatKhauBacSy(Model model, @AuthenticationPrincipal CustomUserDetails taiKhoan) {
        if (taiKhoan == null || !taiKhoan.hasRole("BAC_SY")) {
            return "redirect:/bacsy/login";
        }
        BacSy bacSy = bacSyRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());

        model.addAttribute("bacSy", bacSy);
        model.addAttribute("doiMatKhauRequest", new DoiMatKhauRequest());
        return "bacsy/bacsy_doi_mat_khau";
    }

    @PostMapping("bacsy/doiMatKhau")
    @PreAuthorize("hasAuthority('BACSY')")
    @Transactional
    public String updateMatKhauBacSy(@ModelAttribute @Valid DoiMatKhauRequest doiMatKhauRequest,
                                     BindingResult bindingResult,
                                     Model model, @AuthenticationPrincipal CustomUserDetails taiKhoan,
                                     RedirectAttributes redirectAttributes, Errors errors) {

//        System.out.println(doiMatKhauRequest.toString());
        if (taiKhoan == null || !taiKhoan.hasRole("BAC_SY")) {
            return "redirect:/bacsy/login";
        }

        //validate đầu vào
        if (errors.hasErrors()) {
            return "bacsy/bacsy_doi_mat_khau";
        }

        // check mật khẩu hiện tại
        if (!doiMatKhauRequest.getMatKhauHienTai().equals(taiKhoan.getPassword())) {
            redirectAttributes.addFlashAttribute("loiMatKhau1", "Mật khẩu hiện tại sai mật khẩu cũ");
            return "redirect:/bacsy/doiMatKhau";
        }
        // update mật khẩu mới
        taiKhoanRepo.updateMatKhau(doiMatKhauRequest.getMatKhauMoi(), taiKhoan.getTaiKhoan().getUsername());
        model.addAttribute("ok", "Update mật khẩu thành công");
        return "bacsy/bacsy_doi_mat_khau";
    }

    @GetMapping("bacsy/xemThongKeLichKham")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String thongKeLichKham(Model model, @AuthenticationPrincipal CustomUserDetails taiKhoan) {
        if (taiKhoan == null || !taiKhoan.hasRole("BAC_SY")) {
            return "redirect:/bacsy/login";
        }
        BacSy bacSy = bacSyRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        List<LichKham> lichKhamList = lichKhamRepo.getLichKhamTrongTuan(bacSy.getId());
        model.addAttribute(lichKhamList);
        return "bacsy/bacsy_thong_ke_lich_kham";
    }

    @PostMapping("bacsy/daKham/{id}")
    @PreAuthorize("hasAuthority('BAC_SY')")
    public String daKhamLichKham(RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails taiKhoan,
                                 @PathVariable Integer id) {
        if (taiKhoan == null || !taiKhoan.hasRole("BAC_SY")) {
            return "redirect:/bacsy/login";
        }
        BacSy bacSy = bacSyRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        LichKham lichKham = lichKhamRepo.findById(id).get();
        lichKham.setStatus(Status.DA_KHAM);
        lichKhamRepo.save(lichKham);
        redirectAttributes.addFlashAttribute("ok", "Chuyển trạng thái đã khám thành công");
        return "redirect:/bacsy/xemChiTietLichKham?id=" + id;
    }
}
