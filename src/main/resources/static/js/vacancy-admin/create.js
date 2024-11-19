var editorInstance; // Variable to store CKEditor instance

$(document).ready(function () {
  // Initialize CKEditor for the "description" textarea
  ClassicEditor.create(document.querySelector("#editor"))
    .then((editor) => {
      editorInstance = editor; // Store the CKEditor instance
      editor.setData(""); // Set initial content (optional)
    })
    .catch((error) => {
      console.error(error);
    });

  $(".select2").select2();
});

function submit() {
  // Validate form fields
  var title = $("#title").val();
  var position = $("#position").val();
  var status = $("#status").val();
  var expiredDate = $("#expiredDate").val();
  var requiredPositions = $("#required-positions").val();

  // Check if editor instance is available
  if (!editorInstance) {
    console.error(
      "CKEditor instance not found. Please ensure it is initialized properly."
    );
    return;
  }

  var description = editorInstance.getData();

  // Clear previous validation messages
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
    // Add "is-invalid" class to the fields that are not filled
    if (!title) $("#title").addClass("is-invalid");
    if (!position) $("#position").addClass("is-invalid");
    if (!requiredPositions) $("#required-positions").addClass("is-invalid");
    if (!status) $("#status").addClass("is-invalid");
    if (!expiredDate) $("#expiredDate").addClass("is-invalid");
    if (!description) $("#editor").addClass("is-invalid");
    return;
  }

  // Prepare data for submission
  let data = {
    title: title,
    position: position,
    status: status,
    requiredPositions: requiredPositions,
    expiredDate: expiredDate,
    description: description,
    // jobId: jobId
  };

  // Confirm submission with SweetAlert
  showConfirmAlert(
    "Apakah Anda yakin ingin membuat lowongan ini?",
    function (isConfirmed) {
      if (isConfirmed) {
        // Perform AJAX request to submit the form data
        $.ajax({
          url: "/api/job",
          method: "POST",
          dataType: "JSON",
          contentType: "application/json",
          data: JSON.stringify(data),
          beforeSend: addCsrfToken(),
          success: function (result) {
            // Handle success response
            showSuccessAlert("Lowongan berhasil dibuat", function () {
              // Redirect or reload the page after successful submission
              window.location.href = "/job";
            });
          },
          error: function (error) {
            // Handle error response
            showErrorAlert("Lowongan gagal dibuat");
          },
          complete: function () {
            // Hide loading overlay or perform any cleanup
            $.LoadingOverlay("hide");
          },
        });

        // Show loading overlay during the AJAX request
        $.LoadingOverlay("show");
      }
    }
  );
}

// Function to show success alert
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

// Function to show error alert
function showErrorAlert(message) {
  Swal.fire({
    icon: "error",
    title: "Error",
    text: message,
  });
}

// Function to show confirmation alert
function showConfirmAlert(message, callback) {
  Swal.fire({
    title: message,
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    // Read more about isConfirmed, isDenied below
    if (result.isConfirmed && typeof callback === "function") {
      callback(true);
    } else {
      callback(false);
    }
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
      // Jika pengguna memilih "Ya", maka arahkan kembali ke halaman job
      window.location.href = "/job";
    }
  });
  return false;
}
