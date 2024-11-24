$(document).ready(function () {});

function submit() {
  // Menghapus pesan invalid sebelum validasi
  $(".is-invalid").removeClass("is-invalid");

  var accountId = $("#accountId").val();
  var username = $("#username").val();
  var password = $("#password").val();
  var roleId = $("#roleId").val();

  var firstName = $("#firstName").val();
  var lastName = $("#lastName").val();
  var email = $("#email").val();
  var phone = $("#phone").val();
  var baseBudget = $("#baseBudget").val();
  var limitBudget = $("#limitBudget").val();

  var instituteId = $("#instituteId").val();

  // Validasi sederhana untuk kolom yang wajib diisi
  if (
    !username ||
    !password ||
    !firstName ||
    !email ||
    !roleId ||
    !instituteId
  ) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    // Menandai kolom yang tidak diisi dengan kelas is-invalid
    if (!username) $("#username").addClass("is-invalid");
    if (!password) $("#password").addClass("is-invalid");
    if (!firstName) $("#firstName").addClass("is-invalid");
    if (!email) $("#email").addClass("is-invalid");
    if (!roleId) $("#roleId").addClass("is-invalid");
    if (!instituteId) $("#instituteId").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    username: username,
    password: password,
    firstName: firstName,
    lastName: lastName,
    email: email,
    phone: phone,
    baseBudget: baseBudget,
    limitBudget: limitBudget,
    roleId: roleId,
    instituteId: instituteId,
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
      update(accountId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/api/account/${id}`,
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
        title: "Akun berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/account/";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      $.LoadingOverlay("hide");

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Akun gagal diperbarui",
        showConfirmButton: true,
      });
    },
  });
}

function confirmBack() {
  Swal.fire({
    title: "Apakah anda yakin ingin kembali?",
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: "Tidak",
  }).then((result) => {
    if (result.isConfirmed) {
      window.location.href = "/admin/parameter";
    }
  });
  return false;
}
