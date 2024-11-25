$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

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
    title: "Apakah Anda yakin ingin membuat 'Akun' ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      create(data);
    }
  });
}

function create(data) {
  $.ajax({
    url: `/admin/account/create`,
    method: "POST",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Akun baru berhasil ditambahkan",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/account";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Akun baru gagal ditambahkan",
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
      window.location.href = "/admin/account";
    }
  });
  return false;
}
