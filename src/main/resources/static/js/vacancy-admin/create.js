var editorInstance;

$(document).ready(function () {
  ClassicEditor.create(document.querySelector("#editor"))
    .then((editor) => {
      editorInstance = editor;
      editor.setData("");
    })
    .catch((error) => {
      console.error(error);
    });

  $(".select2").select2();
});

function submit() {
  var title = $("#title").val();
  var position = $("#position").val();
  var status = $("#job-status").val();
  var expiredDate = $("#expiredDate").val();
  var requiredPositions = $("#required-positions").val();

  if (!editorInstance) {
    console.error(
      "CKEditor instance not found. Please ensure it is initialized properly."
    );
    return;
  }

  var description = editorInstance.getData();

  $(".is-invalid").removeClass("is-invalid");

  if (requiredPositions <= 0) {
    document.getElementById("invalid-required").innerHTML =
      "Masukan Bilangan Bulat Positif";
    $("#required-positions").addClass("is-invalid");
    return;
  }

  if (
    !title ||
    !position ||
    !expiredDate ||
    !description ||
    !status ||
    !requiredPositions
  ) {
    showErrorAlert("Semua kolom dengan label required harus diisi");
    if (!title) $("#title").addClass("is-invalid");
    if (!position) $("#position").addClass("is-invalid");
    if (!requiredPositions) $("#required-positions").addClass("is-invalid");
    if (!status) $("#job-status").addClass("is-invalid");
    if (!expiredDate) $("#expiredDate").addClass("is-invalid");
    if (!description) $("#editor").addClass("is-invalid");
    return;
  }

  let data = {
    title: title,
    position: position,
    status: status,
    requiredPositions: requiredPositions,
    expiredDate: expiredDate,
    description: description,
  };

  showConfirmAlert(
    "Apakah Anda yakin ingin membuat 'Lowongan' ini?",
    function (isConfirmed) {
      if (isConfirmed) {
        $.ajax({
          url: "/admin/vacancy/create",
          method: "POST",
          dataType: "JSON",
          contentType: "application/json",
          data: JSON.stringify(data),
          success: function (result) {
            showSuccessAlert("Lowongan berhasil ditambahkan", function () {
              window.location.href = "/admin/vacancy";
            });
          },
          error: function (error) {
            showErrorAlert("Lowongan gagal ditambahkan");
          },
          complete: function () {
            $.LoadingOverlay("hide");
          },
        });
        $.LoadingOverlay("show");
      }
    }
  );
}

function showSuccessAlert(message, callback) {
  Swal.fire({
    position: "center",
    icon: "success",
    title: message,
    showConfirmButton: true,
  }).then((result) => {
    if (result.isConfirmed && typeof callback === "function") {
      callback();
    }
  });
}

function showErrorAlert(message) {
  Swal.fire({
    icon: "error",
    title: message,
    showConfirmButton: true,
  });
}

function showConfirmAlert(message, callback) {
  Swal.fire({
    title: message,
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: "Tidak",
  }).then((result) => {
    if (result.isConfirmed && typeof callback === "function") {
      callback(true);
    } else {
      callback(false);
    }
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
      window.location.href = "/admin/vacancy";
    }
  });
  return false;
}
