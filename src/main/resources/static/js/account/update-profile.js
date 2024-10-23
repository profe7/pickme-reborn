$(document).ready(function () {});

function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var username = $("#username").val();
  var firstName = $("#firstName").val();
  var lastName = $("#lastName").val();
  var email = $("#email").val();
  var phone = $("#phone").val();

  // Validasi sederhana untuk kolom yang wajib diisi
  if (!username || !firstName || !email) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!username) $("#username").addClass("is-invalid");
    if (!firstName) $("#firstName").addClass("is-invalid");
    if (!email) $("#email").addClass("is-invalid");

    return;
  }

   // Validasi format email menggunakan regular expression
   var emailRegex = /\S+@\S+\.\S+/;
   if (!emailRegex.test(email)) {
     Swal.fire({
       icon: "error",
       title: "Error",
       text: "Format email tidak valid",
     });
 
     $("#email").addClass("is-invalid");
 
     return;
   }

    // Untuk validasi format nomor telepon (Min 10 digit Max 13 digit)
    if (phone && !/^(^0)(\d{9,12})$/.test(phone)) {
      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Format nomor telepon tidak valid (contoh: 081234567890)",
      });
  
      $("#phone").addClass("is-invalid");
  
      return;
    }
 

  var username = $("#username").val();
  var firstName = $("#firstName").val();
  var lastName = $("#lastName").val();
  var email = $("#email").val();
  var phone = $("#phone").val();

  var data = JSON.stringify({
    username: username,
    firstName: firstName,
    lastName: lastName,
    email: email,
    phone: phone,
  });

  Swal.fire({
    title: "Apakah anda ingin menyimpan perubahan ini?",
    showCancelButton: true,
    confirmButtonText: "Simpan",
    cancelButtonText: `Batal`,
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(data);
    }
  });
}

function update(data) {
  $.ajax({
    url: `/api/account/update-profile`,
    method: "PUT",
    dataType: "JSON",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: data,
    success: (result) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "success",
        title: "Profil berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        window.history.back();
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Profil gagal diperbarui",
        showConfirmButton: true,
      });
    },
  });
}
