package com.booking_care.security.bac_sy;

import com.booking_care.repository.TaiKhoanRepository;
import com.booking_care.model.TaiKhoan;
import com.booking_care.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class  BacSyDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepo.findByUsername(username);
        if(taiKhoan != null && taiKhoan.hasRole("BAC_SY")){
            return new CustomUserDetails(taiKhoan);
        }
        throw new UsernameNotFoundException(
                "Admin '" + username + "' not found");
    }
}
