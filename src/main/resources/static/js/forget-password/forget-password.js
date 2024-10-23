$(document).ready(function () {
  // Definisikan baseUrl
  const baseUrl = "http://localhost:9000/api/v1"; // Sesuaikan dengan URL backend Anda

  $("#forget-password-form").on("submit", function (e) {
    e.preventDefault(); // Mencegah pengiriman formulir secara default

    const emailOrUsername = $("#email").val();

    $.ajax({
      type: "POST",
      url: `${baseUrl}/forget-password`, // Menggunakan baseUrl untuk membangun URL
      data: { emailOrUsername: emailOrUsername },
      success: function (response) {
        // Tampilkan pesan sukses dari backend
        Swal.fire({
          title: "Sukses!",
          text: response.message,
          icon: "success",
        });
      },
      error: function (xhr, status, error) {
        // Tampilkan pesan error jika ada masalah
        Swal.fire({
          title: "Error!",
          text: "Terjadi kesalahan saat mengirim email.",
          icon: "error",
        });
      },
    });
  });
});
