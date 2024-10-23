$(document).ready(function () {
  $(".select2").select2();
});

var currentPage = 0; // Halaman saat ini
var totalPage = 1;
let inviteButton = $("#inviteButton");

function displayPaginationLinks(currentPage, totalPage, param) {
  var pagination = $("#pagination");
  pagination.empty();

  var numLinks = 2; // Jumlah tautan pagination sebelum dan sesudah currentPage

  // Tautan "First Page" (sembunyikan jika di halaman pertama)
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(0, '${param.job}','${param.skill}')"><<</a></li>`
    );
  }

  // Tautan "Previous"
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${currentPage - 1
      }, '${param.job}','${param.skill}')"><</a></li>`
    );
  }

  // Tampilkan tautan sebelum currentPage
  for (var i = currentPage - numLinks; i < currentPage; i++) {
    if (i >= 0) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i}, '${param.job
        }','${param.skill}')">${i + 1}</a></li>`
      );
    }
  }

  // Tautan currentPage
  pagination.append(
    `<li class="page-item active"><span class="page-link">${currentPage + 1
    }</span></li>`
  );

  // Tampilkan tautan sesudah currentPage
  for (var i = currentPage + 1; i <= currentPage + numLinks; i++) {
    if (i < totalPage) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i}, '${param.job
        }','${param.skill}')">${i + 1}</a></li>`
      );
    }
  }

  // Tautan "Next"
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${currentPage + 1
      }, '${param.job}','${param.skill}')">></a></li>`
    );
  }

  // Tautan "Last Page" (sembunyikan jika di halaman terakhir)
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${totalPage - 1
      }, '${param.job}','${param.skill}')">>></a></li>`
    );
  }
}

// Fungsi untuk mengupdate informasi pagination
function updatePaginationInfo(currentPage, totalPage, data, param) {
  $("#paginationInfo").text(
    `Halaman ${currentPage + 1} dari ${totalPage}, Total Data: ${data.pageData.total
    }`
  );

  if (currentPage > 0) {
    $("#prevPageBtn").show();
  } else {
    $("#prevPageBtn").hide();
  }

  if (currentPage < totalPage - 1) {
    $("#nextPageBtn").show();
  } else {
    $("#nextPageBtn").hide();
  }

  displayPaginationLinks(currentPage, totalPage, param);
}

// Event handler untuk input pencarian dan pemantauan perubahan status
$("#searchJob, #searchSkill").on("change", function () {
  var searchJob = $("#searchJob").val();
  var searchSkill = $("#searchSkill").val();

  displayData(0, searchJob, searchSkill);
});

// function detail(id) {
//   $("#detailCard").LoadingOverlay("show");
//   console.log(id);
//   $.ajax({
//     url: `/api/talent/${id}`,
//     method: "GET",
//     success: (result) => {
//       let root = $("#detailCard");
//       var iframe = $("#talentDetail");
//       var inviteButton = $("#inviteButton");
//       iframe.attr("src", `/talent/share/${id}`);
//       iframe.show();
//       console.log(result);
//       // Pemeriksaan status disabled
//       if (result.invited) {
//         inviteButton
//           .removeClass("invite-button")
//           .addClass("invited-button")
//           .addClass("disabled-button")
//           .find(".fa-envelope") // Ganti ikon
//           .removeClass("fa-envelope")
//           .addClass("fa-envelope-open")
//           .removeClass("invite-button")
//           .addClass("invited-button")
//           .addClass("disabled-button")
//           .find(".fa-envelope") // Ganti ikon
//           .removeClass("fa-envelope")
//           .addClass("fa-envelope-open");
//         inviteButton.find(".invite-button-text").text("Invited"); // Ganti teks
//       } else {
//         inviteButton
//           .removeClass("invited-button")
//           .removeClass("disabled-button")
//           .addClass("invite-button")
//           .find(".fa-envelope-open") // Ganti ikon
//           .removeClass("fa-envelope-open")
//           .addClass("fa-envelope")
//           .removeClass("invited-button")
//           .removeClass("disabled-button")
//           .addClass("invite-button")
//           .find(".fa-envelope-open") // Ganti ikon
//           .removeClass("fa-envelope-open")
//           .addClass("fa-envelope");
//         inviteButton.find(".invite-button-text").text("Invite"); // Ganti teks
//       }
//       root.find(".invite-button").attr("onclick", `invite('${result.id}')`);

//       $("#detailCard").LoadingOverlay("hide", true);
//     },
//     error: (e) => {
//       $("#detailCard").LoadingOverlay("hide", true);
//       inviteButton.show();
//       Swal.fire({
//         position: "center",
//         icon: "error",
//         title: "Request gagal dibuat",
//         showConfirmButton: false,
//         timer: 1500,
//       });
//       x;
//     },
//   });
// }

function invite(talentId, recommendationId) {
  if (recommendationId == null || recommendationId == undefined) {
    recommendationId = null; // Set nilai default null jika recommendationId tidak diberikan
  }

  var data = JSON.stringify({ id: talentId });
  $.ajax({
    type: "POST",
    url: `/api/talent/encrypt`,
    data: data,
    dataType: "text",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    success: function (response) {
      if (recommendationId !== null) {
        console.log(recommendationId);
        window.location.href = `/interview-schedule/create?token=${response}&recommendationId=${recommendationId}`;
      } else {
        window.location.href = `/interview-schedule/create?token=${response}`;
      }
    },
    a,
  });
}

// Fungsi untuk menampilkan data berdasarkan halaman
// Fungsi untuk menampilkan data berdasarkan halaman
function displayData(page, job = "", skill = "", name = "", mitraId = "") {
  $("#detailCard").LoadingOverlay("show");
  $("#listTalent").LoadingOverlay("show");
  let talentDetailNoData = $("#talentDetailNoData");
  talentDetailNoData.hide();
  let root = $("#detailCard");
  root.LoadingOverlay("show");
  var iframe = $("#talentDetail");
  var urlTalent = `/api/talent`;

  var param = {
    job: job,
    skill: skill,
    name: name,
    mitraId: mitraId,
    currentPage: page,
    perPage: 10
  };

  $.ajax({
    type: "GET",
    url: urlTalent,
    data: param,
    dataType: "json",
    success: function (response) {
      console.log(response);
      var data = response;
      var tbody = $("#talentTbl");
      var inviteButton = $("#inviteButton");
      totalPage =
        data.pageData.total == 10
          ? data.pageData.lastPage
          : data.pageData.lastPage + 1;
      currentPage = page;
      updatePaginationInfo(currentPage, totalPage, data, param);
      var startNum = page * data.pageData.perPage + 1;
      tbody.empty();

      if (data.data.length === 0) {
        var messageCard = `
          <div class="card rounded-5">
            <div class="card-body">
              <div class="card-title text-center"><h4>Talents tidak ditemukan.</h4></div>
            </div>
          </div>
        `;
        tbody.append(`<tr><td colspan="100%">${messageCard}</td></tr>`);
        iframe.hide();
        talentDetailNoData.show();
        inviteButton.hide();

      } else {
        // Tampilkan gambar dari tabel talent di kolom photo
        $.each(data.data, function (index, talent) {
          console.log(talent);

          var row = $("<tr>");
          let newCell = $("<td>").html(`
            <div class="card rounded-5 talent-list-card my-1 mx-1 d-flex align-items-center">
              <div class="rounded-image object-fit-cover ml-4">
                <img src="data:image/png;base64,${talent.photo}" alt="Talent Photo" style="width: 100px; height: 100px; object-fit: cover;">
              </div>

              <div class="card-body d-flex justify-content-between align-items-center" style="width: 100%;">
                <div class="talent-info">
                  <h6 class="card-title mb-2 ml-3">${talent.name}</h6>
                  <p class="card-text mb-2 ml-3">${talent.skill ? talent.skill.join(", ") : "No Skills"}</p>
                </div>

                <!-- Button Undang Wawancara -->
                <div class="ml-auto">
                  <button type="button" class="btn d-flex flex-column align-items-center justify-content-center"
                    style="background-color: #00708F; color: white; width: 150px; height: 100px; border-radius: 10px;">
                    <i class="fas fa-envelope" style="font-size: 24px; margin-bottom: 5px;"></i>
                    <span>Undang</span>
                    <span>Wawancara</span>
                  </button>
                </div>
              </div>
            </div>
          `);
          
          // Event handler untuk tombol Undang Wawancara
          newCell.find(".btn").on("click", function () {
            var selectedDate = $("#tanggal").val();
            var selectedStartTime = $("#waktuMulai").val();
            var selectedEndTime = $("#waktuSelesai").val();

            $.ajax({
              type: "GET",
              url: `/api/interview-schedule/talent/${talent.id}`,
              success: function (schedules) {
                var hasConflict = schedules.some(function(schedule) {
                  return schedule.date === selectedDate &&
                         ((schedule.startTime <= selectedStartTime && schedule.endTime >= selectedStartTime) ||
                          (schedule.startTime <= selectedEndTime && schedule.endTime >= selectedEndTime));
                });

                if (hasConflict) {
                  alert("Talent ini sudah memiliki jadwal wawancara pada tanggal dan jam yang sama dan tidak dapat dipilih.");
                } else {
                  $("#scheduleInterviewModal").modal("show");
                }
              },
              error: function () {
                alert("Gagal memeriksa jadwal wawancara.");
              }
            });
          });

          row.append(newCell);
          tbody.append(row);
        });
      }
      $("#detailCard").LoadingOverlay("hide", true);
      $("#listTalent").LoadingOverlay("hide");
    },
    error: function () {
      $("#detailCard").LoadingOverlay("hide", true);
      $("#listTalent").LoadingOverlay("hide");
      talentDetailNoData.show();
      iframe.hide();
      inviteButton.hide();
    },
  });
}

// Event handler untuk tombol "Previous" dan "Next"
$("#prevPageBtn").click(function () {
  var searchJob = $("#searchJob").val();
  var searchSkill = $("#searchSkill").val();
  if (currentPage > 0) {
    displayData(currentPage - 1, searchJob, searchSkill);
  }
});

$("#nextPageBtn").click(function () {
  var searchJob = $("#searchJob").val();
  var searchSkill = $("#searchSkill").val();
  if (currentPage < totalPage - 1) {
    displayData(currentPage + 1, searchJob, searchSkill);
  }
});

// Panggil displayData untuk menampilkan data pertama kali
displayData(currentPage);

$(function () {
  $('[data-toggle="tooltip"]').tooltip();
});

function closeModal() {
  var modal = bootstrap.Modal.getInstance(document.getElementById('scheduleInterviewModal'));
  if (modal) {
      modal.hide();
  }
}



// Event handler untuk tombol "Previous" dan "Next"
$("#prevPageBtn").click(function () {
  var searchJob = $("#searchJob").val();
  var searchSkill = $("#searchSkill").val();
  if (currentPage > 0) {
    displayData(currentPage - 1, searchJob, searchSkill);
  }
});

$("#nextPageBtn").click(function () {
  var searchJob = $("#searchJob").val();
  var searchSkill = $("#searchSkill").val();
  if (currentPage < totalPage - 1) {
    displayData(currentPage + 1, searchJob, searchSkill);
  }
});

// Panggil displayData untuk menampilkan data pertama kali
displayData(currentPage);

$(function () {
  $('[data-toggle="tooltip"]').tooltip();
});


function closeModal() {
  var modal = bootstrap.Modal.getInstance(document.getElementById('scheduleInterviewModal'));
  if (modal) {
    modal.hide();
  }
}


$(document).ready(function () {
  $("#interviewForm").on("submit", function (event) {
    event.preventDefault();
    if (!this.checkValidity()) {
      event.stopPropagation();
    } else {
      const requestData = {
        date: $("#tanggal").val(),
        startTime: $("#waktuMulai").val(),
        endTime: $("#waktuSelesai").val(),
        locationAddress: $("#alamat").val(),
        interviewLink: $("#linkInterview").val(),
      };

      $.ajax({
        type: "POST",
        url: "/interview-schedule",
        contentType: "application/json",
        data: JSON.stringify(requestData),
        success: function (response) {
          alert(response);
          $('#scheduleInterviewModal').modal('hide');
        },
        error: function (xhr) {
          alert(xhr.responseText);
        }
      });
    }
    this.classList.add("was-validated");
  });
});


