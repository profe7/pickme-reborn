$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var parameterId = $("#parameterId").val();
  var reference_group1 = $("#param1").val();
  var reference_group2 = $("#param2").val();
  var reference_name = $("#value-param").val();

  if (!reference_group1 || !reference_name) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    if (!reference_group1) $("#param1").addClass("is-invalid");
    if (!reference_name) $("#value-param").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    reference_group1: reference_group1,
    reference_group2: reference_group2,
    reference_name: reference_name,
  });

  Swal.fire({
    title: "Apakah Anda yakin ingin memperbarui 'Parameter' ini?",
    showCancelButton: true,
    confirmButtonText: "Simpan",
    cancelButtonText: `Batal`,
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(parameterId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/admin/parameter/update/${id}`,
    method: "PUT",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Parameter berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/parameter";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Parameter gagal diperbarui",
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
      window.location.href = "/admin/parameter";
    }
  });
  return false;
}
