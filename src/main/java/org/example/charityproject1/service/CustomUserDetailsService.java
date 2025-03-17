package org.example.charityproject1.service;

import org.example.charityproject1.model.Utilisateurs;
import org.example.charityproject1.repository.UtilisateursRepository;
import org.example.charityproject1.security.RegularUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateurs utilisateur = utilisateursRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Return a RegularUserDetails instance for a regular user
        return new RegularUserDetails(utilisateur.getEmail(), utilisateur.getPassword());
    }
}

