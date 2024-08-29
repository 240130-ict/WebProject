package com.mysite.sbb.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteuser = userRepository.findByUsername(username);
        if (_siteuser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        SiteUser siteUser = _siteuser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 사용자 엔티티 role에 따라 권한 설정
        authorities.add(new SimpleGrantedAuthority("ROLE_" + siteUser.getRole().name()));

        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }

}
