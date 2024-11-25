function submit() {
  $(".is-invalid").removeClass("is-invalid");

  var roleId = $("#roleId").val();
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
    title: "Apakah Anda yakin ingin memperbarui 'Role' ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: "Tidak",
  }).then((result) => {
    if (result.isConfirmed) {
      $.LoadingOverlay("show");
      update(roleId, data);
    }
  });
}

function update(id, data) {
  $.ajax({
    url: `/admin/role/update/${id}`,
    method: "PUT",
    dataType: "JSON",
    contentType: "application/json",
    data: data,
    success: (result) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "success",
        title: "Role berhasil diperbarui",
        showConfirmButton: true,
      }).then(() => {
        window.location.href = "/admin/role";
      });
    },
    error: (e) => {
      $.LoadingOverlay("hide");
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Role gagal diperbarui",
        showConfirmButton: true,
      });
    },
  });
}
