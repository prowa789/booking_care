package com.booking_care.security.benh_nhan;

import com.booking_care.repository.TaiKhoanRepository;
import com.booking_care.model.TaiKhoan;
import com.booking_care.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepo.findByUsername(username);
        if(taiKhoan != null && taiKhoan.hasRole("BENH_NHAN")){
            return new CustomUserDetails(taiKhoan);
        }
        throw new UsernameNotFoundException(
                "Benh Nhan '" + username + "' not found");
    }
}
