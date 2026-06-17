document.addEventListener('DOMContentLoaded', function() {
    // Check for error/success params in URL
    const urlParams = new URLSearchParams(window.location.search);
    const errorCode = urlParams.get('error');
    const successMsg = urlParams.get('success');

    if (errorCode || successMsg) {
        if (errorCode) {
            showToast('Invalid username/email or password. Please try again.', 'danger');
        }
        
        if (successMsg === 'registered') {
            showToast('Registration successful! Please check your email to verify.', 'success');
        } else if (successMsg === 'verified') {
            showToast('Email verified successfully. You can now login.', 'success');
        } else if (successMsg === 'password_reset') {
            showToast('Password reset successful! You can now login with your new password.', 'success');
        } else if (successMsg === 'created') {
            showToast('Created successfully.', 'success');
        } else if (successMsg === 'updated') {
            showToast('Updated successfully.', 'success');
        } else if (successMsg === 'deleted') {
            showToast('Deleted successfully.', 'success');
        }
    }
});

function showToast(message, type) {
    // Create toast container if it doesn't exist
    let toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toastContainer';
        toastContainer.className = 'toast-container position-fixed top-0 end-0 p-3';
        toastContainer.style.zIndex = '9999';
        document.body.appendChild(toastContainer);
    }

    const toastId = 'toast-' + Date.now();
    const bgClass = type === 'danger' ? 'bg-danger text-white' : 'bg-success text-white';
    
    const toastHTML = `
        <div id="${toastId}" class="toast align-items-center ${bgClass} border-0" role="alert" aria-live="assertive" aria-atomic="true">
          <div class="d-flex">
            <div class="toast-body fw-medium">
              ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
          </div>
        </div>
    `;
    
    toastContainer.insertAdjacentHTML('beforeend', toastHTML);
    const toastElement = document.getElementById(toastId);
    
    // Check if Bootstrap is available
    if (typeof bootstrap !== 'undefined') {
        const toast = new bootstrap.Toast(toastElement, { delay: 5000 });
        toast.show();
    } else {
        // Fallback if bootstrap JS is not loaded yet
        toastElement.classList.add('show');
        setTimeout(() => toastElement.classList.remove('show'), 5000);
    }
}
