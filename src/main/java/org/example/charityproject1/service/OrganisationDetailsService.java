package org.example.charityproject1.service;

import org.example.charityproject1.model.Organisations;
import org.example.charityproject1.repository.OrganisationsRepository;
import org.example.charityproject1.security.OrganisationUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OrganisationDetailsService implements UserDetailsService {

    @Autowired
    private OrganisationsRepository organisationsRepository;

    @Override
    public UserDetails loadUserByUsername(String numeroIdentif) throws UsernameNotFoundException {
        Organisations organisation = organisationsRepository.findByNumeroIdentif(numeroIdentif)
                .orElseThrow(() -> new UsernameNotFoundException("Organisation not found: " + numeroIdentif));

        System.out.println("✅ Organisation Found: " + organisation.getNumeroIdentif());
        // Return an OrganisationUserDetails instance for an organization
        return new OrganisationUserDetails(organisation.getNumeroIdentif(), organisation.getPassword());
    }
}