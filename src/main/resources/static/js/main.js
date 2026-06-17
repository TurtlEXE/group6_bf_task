document.addEventListener('DOMContentLoaded', function() {
  // Sticky Navbar Effect on Scroll
  const navbar = document.querySelector('.navbar');
  
  window.addEventListener('scroll', () => {
    if (window.scrollY > 50) {
      navbar.style.boxShadow = '0 4px 12px rgba(0,0,0,0.1)';
    } else {
      navbar.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.05)';
    }
  });

  // Contact Form Basic Validation
  const contactForm = document.getElementById('contactForm');
  if (contactForm) {
    contactForm.addEventListener('submit', function(e) {
      e.preventDefault();
      
      const name = document.getElementById('name').value;
      const email = document.getElementById('email').value;
      const message = document.getElementById('message').value;
      
      if (name && email && message) {
        // Show success message
        const successMsg = document.createElement('div');
        successMsg.className = 'alert alert-success mt-3';
        successMsg.textContent = 'Thank you for your message! We will get back to you soon.';
        
        contactForm.appendChild(successMsg);
        contactForm.reset();
        
        // Remove after 5 seconds
        setTimeout(() => {
          successMsg.remove();
        }, 5000);
      }
    });
  }
});
