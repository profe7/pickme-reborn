$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var name = $("#name").val();
  var rmId = $("#rmId").val();

  var instituteType = $("#instituteType").val();

  if (!name || !rmId || !instituteType) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    if (!name) $("#name").addClass("is-invalid");
    if (!rmId) $("#rmId").addClass("is-invalid");
    if (!instituteType) $("#instituteType").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    instituteName: name,
    rmId: rmId,
    instituteType: instituteType,
  });

  Swal.fire({
    title: "Apakah Anda yakin ingin membuat 'Instansi' ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: "Tidak",
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      create(data);
    }
  });
}

function create(data) {
  $.ajax({
    url: `/admin/institute/create`,
    method: "POST",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Instansi baru berhasil ditambahkan",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/institute";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Instansi baru gagal ditambahkan",
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
      window.location.href = "/admin/institute";
    }
  });
  return false;
}
