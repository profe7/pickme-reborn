$(document).ready(function() {
  $(".alert-login").hide();
  loginAlert();
})

function validateForm() {
  // Remove the 'is-invalid' class from all elements before validation
  $(".is-invalid").removeClass("is-invalid");

  var username = $("#username").val();
  var password = $("#password").val();

  // Validasi sederhana untuk kolom yang wajib diisi
  if (!username || !password) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!username) $("#username").addClass("is-invalid");
    if (!password) $("#password").addClass("is-invalid");

    return false; // Prevent the form from submitting
  }
  return true;
}

function loginAlert() {
  // Mendapatkan query parameter dari URL
  const urlParams = new URLSearchParams(window.location.search);
  const error = urlParams.get('error');

  if(error === 'true') {
      $(".alert-login").show();
  }
}
