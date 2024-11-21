$(document).ready(function () {});

function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var name = $("#name").val();
  var privilegeIds = [];

  if (!name) {
    Swal.fire({
      icon: "error",
      title: "Error",
      text: "Kolom nama peran harus diisi",
    });

    if (!name) $("#name").addClass("is-invalid");

    return;
  }

  $(".privilegeCheckbox:checked").each(function () {
    privilegeIds.push($(this).val());
  });

  var data = JSON.stringify({
    name: name,
    privilegeIds: privilegeIds,
  });

  Swal.fire({
    title: "Apakah Anda yakin ingin membuat 'Role' ini?",
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
    url: `/admin/role/create`,
    method: "POST",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Role baru berhasil ditambahkan",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/role/";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Role baru gagal ditambahkan",
        showConfirmButton: true,
      });
    },
  });
}
