document.addEventListener('DOMContentLoaded', function() {
    // Dropdown functionality
    const dropdownToggle = document.getElementById('dropdownToggle');
    const dropdownContent = document.querySelector('.dropdown-content');

    // Toggle dropdown when clicking the parameter icon
    dropdownToggle.addEventListener('click', function(e) {
        e.preventDefault();
        dropdownContent.classList.toggle('show');
    });

    // Close dropdown when clicking outside
    window.addEventListener('click', function(e) {
        if (!e.target.matches('.param-icon') && !e.target.matches('.fa-cog')) {
            if (dropdownContent.classList.contains('show')) {
                dropdownContent.classList.remove('show');
            }
        }
    });

    // Profile Modal functionality
    const modal = document.getElementById('profileModal');
    const showProfileBtn = document.getElementById('showProfileModal');
    const closeBtn = document.querySelector('.close');

    const profileView = document.getElementById('profileView');
    const editProfileForm = document.getElementById('editProfileForm');
    const passwordForm = document.getElementById('passwordForm');

    const modifyBtn = document.getElementById('modifyBtn');
    const passwordBtn = document.getElementById('passwordBtn');
    const cancelEditBtn = document.getElementById('cancelEditBtn');
    const cancelPasswordBtn = document.getElementById('cancelPasswordBtn');

    // Check URL parameters for errors
    const urlParams = new URLSearchParams(window.location.search);
    const hasPasswordError = urlParams.get('passwordError') === 'true';
    const hasProfileError = urlParams.get('profileError') === 'true';

    // Check for flash attributes via data attributes
    const passwordError = document.body.getAttribute('data-password-error');
    const profileError = document.body.getAttribute('data-profile-error');

    // Check if we need to show error forms
    if (hasPasswordError || passwordError) {
        modal.style.display = 'block';
        profileView.style.display = 'none';
        editProfileForm.style.display = 'none';
        passwordForm.style.display = 'block';
    } else if (hasProfileError || profileError) {
        modal.style.display = 'block';
        profileView.style.display = 'none';
        editProfileForm.style.display = 'block';
        passwordForm.style.display = 'none';
    }

    // Show modal when clicking "Modifier profil"
    showProfileBtn.addEventListener('click', function(e) {
        e.preventDefault();
        modal.style.display = 'block';
        // Always show the profile view first
        profileView.style.display = 'block';
        editProfileForm.style.display = 'none';
        passwordForm.style.display = 'none';

        // Close the dropdown
        dropdownContent.classList.remove('show');
    });

    // Close modal when clicking on X
    closeBtn.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    // Close modal when clicking outside
    window.addEventListener('click', function(e) {
        if (e.target == modal) {
            modal.style.display = 'none';
        }
    });

    // Show edit profile form when clicking "Modifier" button
    modifyBtn.addEventListener('click', function() {
        profileView.style.display = 'none';
        editProfileForm.style.display = 'block';
        passwordForm.style.display = 'none';
    });

    // Show password form when clicking "Modifier mot de passe" button
    passwordBtn.addEventListener('click', function() {
        profileView.style.display = 'none';
        editProfileForm.style.display = 'none';
        passwordForm.style.display = 'block';
    });

    // Return to profile view when clicking cancel buttons
    cancelEditBtn.addEventListener('click', function() {
        profileView.style.display = 'block';
        editProfileForm.style.display = 'none';
        passwordForm.style.display = 'none';
    });

    cancelPasswordBtn.addEventListener('click', function() {
        profileView.style.display = 'block';
        editProfileForm.style.display = 'none';
        passwordForm.style.display = 'none';
    });
});