$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var messageTemplateId = $("#messageTemplateId").val();
  var type = $("#type").val();
  var message = $("#message").val();

  if (!type || !message) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Semua kolom dengan label required harus diisi",
    });

    if (!type) $("#type").addClass("is-invalid");
    if (!message) $("#message").addClass("is-invalid");

    return;
  }

  var data = JSON.stringify({
    type: type,
    message: message,
  });

  Swal.fire({
    title: "Apakah Anda yakin ingin memperbario 'Template Pesan' ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(messageTemplateId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/admin/message-template/update/${id}`,
    method: "PUT",
    dataType: "JSON",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Template Pesan berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/message-template";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Template Pesan gagal diperbarui",
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
      window.location.href = "/admin/message-template";
    }
  });
  return false;
}
