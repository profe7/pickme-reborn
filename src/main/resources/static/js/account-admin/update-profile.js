$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var username = $("#username").val();
  var firstName = $("#firstName").val();
  var lastName = $("#lastName").val();
  var email = $("#email").val();
  var phone = $("#phone").val();

  if (!username || !firstName || !email) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    if (!username) $("#username").addClass("is-invalid");
    if (!firstName) $("#firstName").addClass("is-invalid");
    if (!email) $("#email").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    username: username,
    firstName: firstName,
    lastName: lastName,
    email: email,
    phone: phone,
  });

  Swal.fire({
    title: "Apakah Anda yakin ingin memperbarui 'Profil' ini?",
    showCancelButton: true,
    confirmButtonText: "Simpan",
    cancelButtonText: `Batal`,
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(data);
    }
  });
}

function update(data) {
  $.ajax({
    url: `/admin/account/update-profile`,
    method: "PUT",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Profil berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/";
      });
    },
    error: (e) => {
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

function confirmBack() {
  Swal.fire({
    title: "Apakah Anda yakin ingin kembali?",
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: "Tidak",
  }).then((result) => {
    if (result.isConfirmed) {
      window.location.href = "/admin";
    }
  });
  return false;
}
