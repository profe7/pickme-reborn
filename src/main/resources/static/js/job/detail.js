var selectedTalents = []; // Simpan ID atau nilai unik dari orang-orang yang sudah dipilih

$(document).ready(function () {
  setIframeHeight();

  // Tangani klik tombol "Select Talent" untuk menampilkan modal
  document
    .getElementById("selectTalentButton")
    .addEventListener("click", function (event) {
      $("#selectTalentModal").modal("show");

      // Dapatkan nilai dari elemen input modal
      var searchName = $("#searchName").val();
      var searchPosition = $("#searchPosition").val();
      var searchSkill = $("#searchSkill").val();

      // Panggil fungsi displayData dengan nilai-nilai tersebut
      displayData(0, searchName, searchPosition, searchSkill, null);

      // Hentikan propagasi event agar modal tidak tertutup
      event.preventDefault();
    });



  const mitraSelect = $("#selectTalent");
  let headerCv = $("#headerCv");
  let jobId = parseInt($("#jobId").text());
  let instituteId = parseInt($("#mitraId").text());
  const outsourcerId = parseInt($("#outsourcerId").text());

  console.log("Job ID:", jobId);
  console.log("Mitra ID:", instituteId);
  console.log("Outsourcer ID:", outsourcerId);

  function displayData() {
    let url = `/talent/availableforjob/${instituteId}/${jobId}`;

    $.ajax({
      type: "GET",
      url: url,
      dataType: "json",
      success: function (response) {
        let data = response;
        let tbody = $("#talentTableBody");
        tbody.empty();

        $.each(data, function (index, talent) {
          let row = $("<tr>");

          row.append($("<td>").text(talent.name));

          let cvStatus;
          let cvStatusClass;

          switch (talent.statusCV) {
            case "COMPLETE":
              cvStatus = "Complete";
              cvStatusClass = "bg-complete";
              break;
            case "DRAFT":
            default:
              cvStatus = "Draft";
              cvStatusClass = "bg-draft";
              break;
          }

          row.append(
            $("<td>")
              .css({
                "background-color": cvStatusClass === "bg-draft" ? "red" : "#8eff8e",
                color: "black",
                "border-radius": "20px",
                padding: "10px 20px",
                "text-align": "center",
                display: "inline-block",
              })
              .text(cvStatus)
          );

          row.append(
            $("<td>").text(
              talent.currentWorkAssignment == null
                ? ""
                : talent.currentWorkAssignment.position
            )
          );

          row.append($("<td>").text(talent.skill));

          tbody.append(row);

          let selectButton = $("<button>")
            .addClass("btn btn-primary btn-sm select-talent-button")
            .text("Pilih")
            .css("margin-left", "10px")
            .on("click", function () {
              let selectedTalentName = talent.name;
              let selectedTalentId = talent.id;
              let talentId = talent.id; // Get the unique ID of the selected person

              // Check if the selected person is already in the previously selected list
              if (selectedTalents.includes(talentId)) {
                // The person has already been selected, show an error message using SweetAlert
                Swal.fire({
                  icon: "error",
                  title: "Oops...",
                  text: "Orang ini sudah dipilih sebelumnya!",
                });
                event.preventDefault();
                return; // Stop further action
              }

              // The person has not been selected before, add to the list and to select2
              $("#selectTalent").append(
                `<option value="${selectedTalentId}" selected>${selectedTalentName}</option>`
              );
              $("#selectTalent").trigger("change"); // Update select2 display after adding new option
              selectedTalents.push(talentId); // Add the unique ID to the selected list
              event.preventDefault();

              // Disable the "Pilih" button after the talent is selected
              $(this).prop("disabled", true);
            });

          let previewButton = $("<button>")
            .addClass("btn btn-info btn-sm preview-cv-button")
            .text("Preview CV")
            .css({
              color: "white", // Change text color to white
            })
            .on("click", function () {
              let talentCvUrl = `https://dev.cv-me.metrodataacademy.id/share/${talent.userUrl}`;
              // Open CV in a new tab
              window.open(talentCvUrl, "_blank");
            });

          row.append($("<td>").append(previewButton).append(selectButton)); // Show "Pilih" and "Preview" buttons
        });
      },
      error: function () {
        // Handle error
      },
    });
  }

  function fetchMitraData() {
    let url = `/api/talent/talent-by-mitra-by-job?userId=${BigInt(
      outsourcerId
    )}&jobId=${BigInt(jobId)}`;
    // let url = `/api/talent/talent-by-mitraAll?mitraId=${instituteId}`
    // console.log("====================================");
    // console.log(url);

    return $.ajax({
      url: url,
      method: "GET",
      dataType: "json",
      success: function (data) {
        // console.log("Data received:", data);
        // console.log("====================================");
      },
      error: function (xhr, status, error) {
        // console.error("Error:", error);
        // console.error("====================================");
      },
    });
  }

  function updateSelect2() {
    fetchMitraData().done(function (mitraData) {
      mitraSelect.empty();
      mitraSelect.select2({
        multiple: true,
        placeholder: "Select talents",
        tags: false,
      });

      mitraSelect.on("change", function (event) {
        var selectedMitraData = mitraSelect.select2("data");

        if (selectedMitraData == 0) {
          $("#talentDetail").hide();
          headerCv.text(" ");
        }

        var lastIndex = selectedMitraData.length - 1;
        displaySelectedMitraNames(selectedMitraData, lastIndex);
      });
      mitraSelect.trigger("change");
    });
  }

  function displaySelectedMitraNames(selectedMitraData, lastIndex) {
    let mitra = selectedMitraData[lastIndex];

    if (selectedMitraData.length > 0 && mitra) {
      headerCv.text(`Preview Cv ${mitra.text}`);
      var iframe = $("#talentDetail");
      iframe.attr(
        "src",
        `https://dev.cv-me.metrodataacademy.id/share/${mitra.userUrl}`
      );
      $("#detailCard").LoadingOverlay("hide", true);
      iframe.show();
      $.ajax({
        url: `/api/talent/${mitra.id}`,
        method: "GET",
        success: (result) => {
          var iframe = $("#talentDetail");
          iframe.attr(
            "src",
            `https://dev.cv-me.metrodataacademy.id/share/${result.userUrl}`
          );
          iframe.show();
          $("#detailCard").LoadingOverlay("hide", true);
        },
        error: (e) => {
          $("#detailCard").LoadingOverlay("hide", true);
          submitButton.show();
          Swal.fire({
            position: "center",
            icon: "error",
            title: "Request gagal dibuat",
            showConfirmButton: false,
            timer: 1500,
          });
        },
      });
    } else {
      $("#talentDetailNoData").show();
    }
  }

  function updateTalentIds() {
    const selectedTalents = mitraSelect.val() || [];
    console.log("Selected Talents:", selectedTalents);
    return selectedTalents;
  }

  $("#applyButton").on(
    "click",
    function () {
      const applyButton = document.getElementById("applyButton");
      const talentIds = mitraSelect.select2("data").map((talent) => talent.id);

      if (talentIds.length === 0) {
        Swal.fire({
          icon: "warning",
          title: "Warning",
          text: "Please select at least one talent before applying.",
        });
        return;
      }

      if (talentIds.length > 0) {
        const postData = {
          jobId: jobId,
          outsourcerId: outsourcerId,
          talentsId: talentIds,
        };

        applyButton.textContent = "Applied";
        $("#applyButton").prop("disabled", true);
        applyButton.setAttribute("disabled", "");
        console.log(postData);

        $.ajax({
          url: "/api/applicant",
          method: "POST",
          beforeSend: addCsrfToken(),
          contentType: "application/json",
          data: JSON.stringify(postData),
          success: function (response) {
            console.log("Applicant data sent successfully:", response);

            Swal.fire({
              icon: "success",
              title: "Success",
              text: "Applicant data sent successfully!",
            }).then(() => {

              window.location.href = "/job/";
            });
          },
          error: function (error) {
            console.error("Error sending applicant data:", error);
            // window.location.reload();

            Swal.fire({
              icon: "error",
              title: "Error",
              text: "Failed to send applicant data. Please try again.",
            });
          },
        });
      } else {
        console.warn("No talents selected!");
      }
    },
    updateSelect2()
  );

  $("#saveNewTalentButton").on("click", function () {
    console.log("Save New Talent button clicked"); // Debugging line
    let namaLengkap = $("#namaLengkap").val();
    let email = $("#surelTalent").val();
    let nomorKTP = $("#nomorKTP").val();

    console.log(instituteId);
    console.log(jobId);
    console.log(outsourcerId);

    $.ajax({
      url: '/talent/verifynik/' + nomorKTP,
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      },
      success: function (response) {
        if (response === 'failed') {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Nomor KTP gagal diverifikasi, sudah terdaftar atau tidak valid',
          });
        } else {
          Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'Nomor KTP berhasil diverifikasi. Melanjutkan pendaftaran...',
            showConfirmButton: false,
            timer: 1500
          }).then(() => {
            let requestData = {
              name: namaLengkap,
              email: email,
              nik: nomorKTP,
              instituteId: instituteId,
              outsourcerId: outsourcerId,
              jobId: jobId
            };

            $.ajax({
              url: '/talent/create-from-job',
              method: 'POST',
              beforeSend: addCsrfToken(),
              contentType: 'application/json',
              data: JSON.stringify(requestData),
              success: function (response) {
                if (response === "success") {
                  Swal.fire("Success", "Talent created and applied for this job!", "success")
                    .then(() => {
                      window.location.reload();
                    });
                } else {
                  Swal.fire("Failed", "There was an error creating the talent", "error");
                }
              },
              error: function (error) {
                if (error.status === 400) {
                  Swal.fire("Error", "Email already exists!", "error");
                } else {
                  Swal.fire("Error", "There was an error creating the talent", "error");
                }
              }
            });
          });
        }
      },
      error: function (error) {
        Swal.fire("Error", "There was an error verifying the nomor KTP", "error");
      }
    });
  });

  
  let mitraId = parseInt($("#mitraId").text());

  //  var url = '/mitra/' + jobId + '/' + mitraId;
   var url = '/api/applicant/mitra/' + jobId + '/' + mitraId;

   $.ajax({
       url: url,
       type: 'GET',
       dataType: 'json',
       success: function(response) {
           var tableBody = $('#tableBody');
           tableBody.empty(); 

           if (response.length > 0) {
               var totalPelamar = response.length;

               
               $.each(response, function(index, applicantNominee) {
                console.log(applicantNominee.talent);
                var talentName = applicantNominee.talent.name; 
                var talentStatusCV = applicantNominee.talent.statusCV;
                
                   var row = '<tr>' +
                             '<td>' + (index + 1) + '</td>' +  
                             '<td>' + talentName + '</td>' +  
                             '<td>' + talentStatusCV + '</td>' + 
                             '</tr>';

                   tableBody.append(row);
               });

               
               $('#totalPelamar').text(totalPelamar);
           } else {
               tableBody.append('<tr><td colspan="3">Tidak ada pelamar</td></tr>');
           }
       },
       error: function(error) {
           console.log("Error:", error);
           $('#tableBody').append('<tr><td colspan="3">Gagal memuat data pelamar</td></tr>');
       }
   });

});

function setIframeHeight() {
  var colHeight = document.querySelector(".detail-job").offsetHeight;
  document.querySelector(".iframe").style.height = colHeight + "px";
}

function redirectToJobDetail(element) {
  const jobId = $(element).data("id");
  window.location.href = "/job/" + jobId;
}

// Tangani klik tombol "Apply"
document
  .getElementById("applySelection")
  .addEventListener("click", function () {
    // Tambahkan logika di sini untuk menangani tindakan berikutnya setelah pemilihan
    $("#selectTalentModal").modal("hide"); // Sembunyikan modal setelah selesai
  });



