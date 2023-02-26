package com.booking_care.controller;

import com.booking_care.constant.Constants;
import com.booking_care.mapstruct.SimpleMapper;
import com.booking_care.model.*;
import com.booking_care.model.request.*;
import com.booking_care.model.response.BacSyDto;
import com.booking_care.model.response.BenhNhanDto;
import com.booking_care.repository.*;
import com.booking_care.utils.FileUploadUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    @Autowired
    BaiDangRepository baiDangRepo;
    @Autowired
    ChuyenMucRepository chuyenMucRepo;
    @Autowired
    ChuyenKhoaRepository chuyenKhoaRepo;
    @Autowired
    TaiKhoanRepository taiKhoanRepo;
    @Autowired
    BenhNhanRepository benhNhanRepo;
    @Autowired
    VaiTroRepository vaiTroRepo;
    @Autowired
    SimpleMapper simpleMapper;
    @Autowired
    BacSyRepository bacSyRepo;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/admin/login")
    public String viewAdminLoginPage() {
        return "admin/admin_login";
    }

    @GetMapping("/admin/home")
    public String viewAdminHomePage() {
        return "admin/admin_home";
    }

    @GetMapping("/admin/chuyenkhoa")
    public String viewChuyenKhoaList(Model model) {
        List<ChuyenKhoa> chuyenKhoaList = chuyenKhoaRepo.findAll();
        model.addAttribute("chuyenKhoaList",chuyenKhoaList);
        System.out.println(chuyenKhoaList);
        return "admin/admin_xem_chuyen_khoa";
    }
    @GetMapping("/admin/chuyenkhoa/{id}")
    public String viewChuyenKhoa(Model model, @PathVariable Integer id) {
        ChuyenKhoa chuyenKhoa = chuyenKhoaRepo.findById(id).get();
        model.addAttribute("chuyenKhoa",chuyenKhoa);
        System.out.println(chuyenKhoa);
        return "admin/admin_sua_chuyen_khoa";
    }
    @PostMapping("/admin/chuyenkhoa/{id}")
    @Transactional
    public String editChuyenKhoa(ChuyenKhoaRequest chuyenKhoaRequest, @PathVariable Integer id) {
        chuyenKhoaRepo.updateChuyenKhoa(chuyenKhoaRequest.getTen(),chuyenKhoaRequest.getMoTa(),id);
        return "redirect:/admin/chuyenkhoa";
    }
    @GetMapping("/admin/chuyenkhoa/delete/{id}")
    public String removeChuyenKhoa(ChuyenKhoaRequest chuyenKhoaRequest, Model model, @PathVariable Integer id) {
        chuyenKhoaRepo.deleteById(id);
        return "redirect:/admin/chuyenkhoa";
    }
    @GetMapping("/admin/chuyenkhoa/add")
    public String addChuyenKhoa(Model model) {
        model.addAttribute("chuyenKhoa", new ChuyenKhoa());
        return "admin/admin_them_chuyen_khoa";
    }
    @PostMapping("/admin/chuyenkhoa/add")
    public String addChuyenKhoa(ChuyenKhoaRequest chuyenKhoaRequest, Model model) {
        chuyenKhoaRepo.save(chuyenKhoaRequest.toChuyenKhoa());
        return "redirect:/admin/chuyenkhoa";
    }

    @GetMapping("/admin/benhnhan")
    public String viewBenhNhanList(Model model) {
        List<BenhNhan> benhNhanList = benhNhanRepo.findAll();
        List<BenhNhanDto> benhNhanDtoList = benhNhanList.stream().map(simpleMapper::toBenhNhanDto).collect(Collectors.toList());
        model.addAttribute("benhNhanList",benhNhanDtoList);
        return "admin/admin_xem_benh_nhan";
    }
    @GetMapping("/admin/benhnhan/{id}")
    public String viewBenhNhan(Model model, @PathVariable Integer id) {
        BenhNhan benhNhan = benhNhanRepo.findById(id).get();
        model.addAttribute("benhNhan",benhNhan);
        return "admin/admin_sua_benh_nhan";
    }
    @PostMapping("/admin/benhnhan/{id}")
    public String editBenhNhan(@PathVariable Integer id,RedirectAttributes redirectAttributes,
                               @ModelAttribute(name = "benhNhan") BenhNhanRequest benhNhanRequest,
                               Errors errors) {
        System.out.println(benhNhanRequest.toString());
        if(errors.hasErrors()) {
            return "admin/admin_sua_benh_nhan";
        }
        try {
            BenhNhan benhNhan = benhNhanRepo.findById(id).get();
            benhNhan.setHoTen( benhNhanRequest.getHoTen() );
            benhNhan.setSdt( benhNhanRequest.getSdt() );
            benhNhan.setEmail( benhNhanRequest.getEmail() );
            benhNhan.setDiaChi( benhNhanRequest.getDiaChi() );
            benhNhan.setNgaySinh(format.parse(benhNhanRequest.getNgaySinh()));
            benhNhanRepo.save(benhNhan);
            redirectAttributes.addFlashAttribute("ok","Update thành công");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin/benhnhan/" + id;
    }
    @GetMapping("/admin/bacsy")
    public String viewBacSyList(Model model) {
        List<BacSy> bacSyList = bacSyRepo.findAll();
        List<BacSyDto> bacSyDtoList = bacSyList.stream().map(simpleMapper::toBacSyDto).collect(Collectors.toList());
        model.addAttribute("bacSyList",bacSyDtoList);
        return "admin/admin_xem_bac_sy";
    }
    @GetMapping("/admin/bacsy/add")
    public String viewAddBacSy(Model model) {
        List<ChuyenKhoa> chuyenKhoaList = chuyenKhoaRepo.findAll();
        model.addAttribute("chuyenKhoaList",chuyenKhoaList);
        model.addAttribute("bacSy", new BacSyResgisterRequest());
        return "admin/admin_them_bac_sy";
    }

    @PostMapping("/admin/bacsy/add")
    public String addBacSy(RedirectAttributes redirectAttributes,
                           @ModelAttribute(name = "bacSy") @Valid BacSyResgisterRequest bacSyRequest) {
        try {
            System.out.println("bacsy request post: " + bacSyRequest.getNgaySinh());
            if(taiKhoanRepo.findByUsername(bacSyRequest.getUsername()) != null) {
                redirectAttributes.addFlashAttribute("loi","Tài khoản đã tồn tại");
                return "redirect:/admin/bacsy/add";
            }
            // tao tai khoan cho benh nhan
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setUsername(bacSyRequest.getUsername());
            taiKhoan.setPassword(bacSyRequest.getPassword());
            taiKhoan.setVaiTro(vaiTroRepo.findById(Constants.VaiTro.ROLE_BAC_SY).get());
            taiKhoanRepo.save(taiKhoan);
            // luu thong tin bac sy
            BacSy bacSy = new BacSy(bacSyRequest.getHoTen(),format.parse(bacSyRequest.getNgaySinh()),bacSyRequest.getSdt(),
                    bacSyRequest.getEmail(),bacSyRequest.getGioiThieu(),bacSyRequest.getChucVu(),chuyenKhoaRepo.findById(bacSyRequest.getChuyenKhoaId()).get(),"",taiKhoan);
            bacSy = bacSyRepo.save(bacSy);
            redirectAttributes.addFlashAttribute("ok","Thêm mới thành công !");
            return "redirect:/admin/bacsy/" + bacSy.getId();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "admin/admin_sua_bac_sy";
    }

    @GetMapping("/admin/bacsy/{id}")
    public String viewBacSy(Model model, @PathVariable Integer id) {
        BacSy bacSy = bacSyRepo.findById(id).get();
        List<ChuyenKhoa> chuyenKhoaList = chuyenKhoaRepo.findAll();
        model.addAttribute("bacSy",bacSy);
        model.addAttribute("chuyenKhoaList",chuyenKhoaList);
        return "admin/admin_sua_bac_sy";
    }

    @Transactional
    @PostMapping("/admin/bacsy/{id}")
    public String updateBacSy(@PathVariable Integer id,RedirectAttributes redirectAttributes,
                            @ModelAttribute(name = "bacSy") @Valid BacSyRequest bacSyRequest, @RequestParam("image") MultipartFile file,
                            Errors errors) {
        if(errors.hasErrors()) {
            return "admin/admin_sua_bac_sy";
        }
        try {
            System.out.println("<< " + file.getName());
            System.out.println("<< " + bacSyRequest.toString());

            System.out.println("bacsy request post: " + bacSyRequest.getNgaySinh());

            if(file.isEmpty()) {
                bacSyRepo.updateThongTinBacSy(format.parse(bacSyRequest.getNgaySinh()),bacSyRequest.getHoTen(),
                        bacSyRequest.getChucVu(),bacSyRequest.getChuyenKhoaId(),bacSyRequest.getGioiThieu(),
                        bacSyRequest.getSdt(),bacSyRequest.getEmail(),bacSyRequest.getId());
                redirectAttributes.addFlashAttribute("ok","Update thành công");
                return "redirect:/admin/bacsy/" + id;
            }
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            bacSyRepo.updateThongTinBacSyAndUploadFile(format.parse(bacSyRequest.getNgaySinh()),bacSyRequest.getHoTen(),
                    bacSyRequest.getChucVu(),bacSyRequest.getChuyenKhoaId(),bacSyRequest.getGioiThieu(),
                    bacSyRequest.getSdt(),bacSyRequest.getEmail(), fileName ,bacSyRequest.getId());
            String uploadDir = "bacsy-photos/" + id;

            FileUploadUtil.saveFile(uploadDir, fileName, file);

            redirectAttributes.addFlashAttribute("ok","Update thành công");
            return "redirect:/admin/bacsy/" + id;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:admin/bacsy/" + id;
    }

    @GetMapping("/admin/bacsy/delete/{id}")
    @Transactional
    public String deleteBacSy(Model model, @PathVariable Integer id) {
        bacSyRepo.deleteById(id);
        return "redirect:/admin/bacsy";
    }


    // thêm sửa xóa bài đăng
    @GetMapping("/admin/baidang")
    public String viewBaiDangList(Model model) {
        List<BaiDang> baiDangList = baiDangRepo.findAll();
        model.addAttribute("baiDangList",baiDangList);
        return "admin/admin_xem_bai_dang";
    }
    @GetMapping("/admin/baidang/{id}")
    public String viewBaiDang(Model model, @PathVariable Integer id) {
        BaiDang baiDang = baiDangRepo.findById(id).get();
        model.addAttribute("baiDang", baiDang);
        return "admin/admin_sua_bai_dang";
    }

    @Transactional
    @PostMapping("/admin/baidang/{id}")
    public String editBaiDang(@PathVariable Integer id, Model model,
                              @ModelAttribute(name = "baiDang") BaiDangRequest baiDangRequest,
                              @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes
                              ) throws IOException {

        System.out.println("<< " + file.getName());
        System.out.println("<< " + baiDangRequest.toString());

        Map<String,String> errors = new HashMap<>();
        if(file.isEmpty()) {
//            errors.put("err2","Chưa chọn file tải lên");
            redirectAttributes.addFlashAttribute("ok","Update thành công");
            baiDangRepo.updateBaiDangKoUploadFile(baiDangRequest.getTen(), baiDangRequest.getChuyenMucId(),
                    baiDangRequest.getTomTat(),baiDangRequest.getNoiDung(), id);
            return "redirect:/admin/baidang/" + id;
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        baiDangRepo.updateBaiDang(baiDangRequest.getTen(), baiDangRequest.getChuyenMucId(),
                baiDangRequest.getTomTat(),baiDangRequest.getNoiDung(),fileName, id);
        String uploadDir = "baidang-photos/" + id;

        FileUploadUtil.saveFile(uploadDir, fileName, file);

        redirectAttributes.addFlashAttribute("ok","Update thành công");
        return "redirect:/admin/baidang/" + id;
    }
    @GetMapping("/admin/baidang/delete/{id}")
    public String deleteBaiDang(Model model, @PathVariable Integer id) {
        bacSyRepo.deleteById(id);
        return "redirect:/admin/baidang";
    }
    @GetMapping("/admin/baidang/add")
    public String viewAddBaiDang(Model model) {
        model.addAttribute("baiDang",new BaiDang());
        return "admin/admin_them_bai_dang";
    }

    @ModelAttribute("chuyenMucList")
    List<ChuyenMuc> getAllChuyenMuc(){
       return chuyenMucRepo.findAll();
    }

    @PostMapping("/admin/baidang/add")
    public String addBaiDang(@ModelAttribute("baiDang") AddBaiDangRequest baiDangRequest,
                             Model model,
                             @RequestParam("image") MultipartFile file) throws IOException {
        Map<String,String> errors = new HashMap<>();
        if(!chuyenMucRepo.existsById(baiDangRequest.getChuyenMucId())) {
            errors.put("err1","Chưa chọn chuyên mục Id");
            return "admin/admin_them_bai_dang";
        }
        if(file.isEmpty()) {
            errors.put("err2","Chưa chọn file tải lên");
            return "admin/admin_them_bai_dang";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        BaiDang baiDang = new BaiDang();
        baiDang.setTen(baiDangRequest.getTen());
        baiDang.setNoiDung(baiDangRequest.getNoiDung());
        baiDang.setChuyenMuc(chuyenMucRepo.findById(baiDangRequest.getChuyenMucId()).get());
        baiDang.setTomTat(baiDangRequest.getTomTat());
        baiDang.setPhoto(fileName);
        BaiDang baiDang1 = baiDangRepo.save(baiDang);

        String uploadDir = "baidang-photos/" + baiDang1.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, file);
        model.addAttribute("errors",errors);
//        model.addAttribute("ok","Thêm mới thành công");
        return "redirect:/admin/baidang";
    }
    // thêm sửa xóa chuyên mục
    @GetMapping("/admin/chuyenmuc")
    public String viewALlChuyenMuc(Model model) {
        List<ChuyenMuc> chuyenMucList = chuyenMucRepo.findAll();
        model.addAttribute("chuyenMuc",chuyenMucList);
        return "admin/admin_xem_chuyen_muc";
    }
    @GetMapping("/admin/chuyenmuc/{id}")
    public String viewChuyenMuc(Model model, @PathVariable Integer id) {
        ChuyenMuc chuyenMuc = chuyenMucRepo.findById(id).get();
        model.addAttribute("chuyenMuc",chuyenMuc);
        return "admin/admin_sua_chuyen_muc";
    }
    @PostMapping("/admin/chuyenmuc/{id}")
    @Transactional
    public String editChuyenMuc(@ModelAttribute("chuyenMuc") ChuyenMuc chuyenMuc, @PathVariable Integer id) {
        chuyenMucRepo.save(chuyenMuc);
        return "redirect:/admin/chuyenmuc";
    }
    @GetMapping("/admin/chuyenmuc/delete/{id}")
    public String removeChuyenMuc( Model model, @PathVariable Integer id) {
        return "redirect:/admin/chuyenmuc";
    }
    @GetMapping("/admin/chuyenmuc/add")
    public String viewAddChuyenMuc(Model model) {
        model.addAttribute("chuyenMuc", new ChuyenMuc());
        return "admin/admin_them_chuyen_muc";
    }
    @PostMapping("/admin/chuyenmuc/add")
    public String addChuyenMuc(ChuyenMuc chuyenMuc, Model model) {
        chuyenMucRepo.save(chuyenMuc);
        return "redirect:/admin/chuyenmuc";
    }

}
