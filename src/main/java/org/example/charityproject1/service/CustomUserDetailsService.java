package org.example.charityproject1.service;

import org.example.charityproject1.model.Organisations;
import org.example.charityproject1.model.SuperAdmin;
import org.example.charityproject1.model.Utilisateurs;
import org.example.charityproject1.repository.OrganisationsRepository;
import org.example.charityproject1.repository.SuperAdminRepository;
import org.example.charityproject1.repository.UtilisateursRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateursRepository utilisateursRepository;
    private final SuperAdminRepository superAdminRepository;
    private final OrganisationsRepository organisationsRepository;

    public CustomUserDetailsService(UtilisateursRepository utilisateursRepository, SuperAdminRepository superAdminRepository, OrganisationsRepository organisationsRepository) {
        this.utilisateursRepository = utilisateursRepository;
        this.superAdminRepository = superAdminRepository;
        this.organisationsRepository = organisationsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to load a regular user
        Optional<Utilisateurs> user = utilisateursRepository.findByEmail(username);
        if (user.isPresent()) {
            return new User(
                    user.get().getEmail(),
                    user.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }

        // Try to load a SuperAdmin
        Optional<SuperAdmin> superAdmin = superAdminRepository.findByEmail(username);
        if (superAdmin.isPresent()) {
            return new User(
                    superAdmin.get().getEmail(),
                    superAdmin.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))
            );
        }

        // Try to load an organisation
        Optional<Organisations> organisation = organisationsRepository.findByNumeroIdentif(username);
        if (organisation.isPresent()) {
            return new User(
                    organisation.get().getNumeroIdentif(),
                    organisation.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ORGANISATION"))
            );
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}