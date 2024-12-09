$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var holidayId = $("#holidayId").val();
  var name = $("#name").val();
  var description = $("#description").val();
  var date = $("#date").val();

  if (!name || !description || !date) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    if (!name) $("#name").addClass("is-invalid");
    if (!description) $("#description").addClass("is-invalid");
    if (!date) $("#date").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    name: name,
    description: description,
    date: date,
  });

  Swal.fire({
    title: "Apakah Anda yakin ingin memperbarui 'Hari Libur' ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(holidayId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/admin/holiday/update/${id}`,
    method: "PUT",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Hari Libur berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/holiday";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Hari Libur gagal diperbarui",
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
      window.location.href = "/admin/holiday";
    }
  });
  return false;
}
