$(document).ready(function () {
  $(".select2").select2();
});

var currentPage = 0; // Halaman saat ini
var totalPage = 1;

function displayPaginationLinks(currentPage, totalPage, param) {
  var pagination = $("#pagination");
  pagination.empty();

  var numLinks = 2; // Jumlah tautan pagination sebelum dan sesudah currentPage

  // Tautan "First Page" (sembunyikan jika di halaman pertama)
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(0, '${param.name}','${param.date}')"><<</a></li>`
    );
  }

  // Tautan "Previous"
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        currentPage - 1
      }, '${param.name}','${param.date}')"><</a></li>`
    );
  }

  // Tampilkan tautan sebelum currentPage
  for (var i = currentPage - numLinks; i < currentPage; i++) {
    if (i >= 0) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i}, '${
          param.name
        }','${param.date}')">${i + 1}</a></li>`
      );
    }
  }

  // Tautan currentPage
  pagination.append(
    `<li class="page-item active"><span class="page-link">${
      currentPage + 1
    }</span></li>`
  );

  // Tampilkan tautan sesudah currentPage
  for (var i = currentPage + 1; i <= currentPage + numLinks; i++) {
    if (i < totalPage) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i}, '${
          param.name
        }','${param.date}')">${i + 1}</a></li>`
      );
    }
  }

  // Tautan "Next"
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        currentPage + 1
      }, '${param.name}','${param.date}')">></a></li>`
    );
  }

  // Tautan "Last Page" (sembunyikan jika di halaman terakhir)
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        totalPage - 1
      }, '${param.name}','${param.date}')">>></a></li>`
    );
  }
}

// Fungsi untuk mengupdate informasi pagination
function updatePaginationInfo(currentPage, totalPage, data, param) {
  $("#paginationInfo").text(
    `Halaman ${currentPage + 1} dari ${totalPage}, Total Data: ${
      data.pageData.total
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

$("#searchName, #searchDate").on("input change", function () {
  var searchName = $("#searchName").val();
  var searchDate = $("#searchDate").val();

  displayData(0, searchName, searchDate);
});

function getCurrentDate() {
  const today = new Date();
  const year = today.getFullYear();
  let month = today.getMonth() + 1;
  let day = today.getDate();

  // Add leading zero to single-digit months and days
  month = month < 10 ? "0" + month : month;
  day = day < 10 ? "0" + day : day;

  return `${year}-${month}-${day}`;
}

// Fungsi untuk menampilkan data berdasarkan halaman
function displayData(page, name = "", date = "") {
  $("#listTalent").LoadingOverlay("show");
  let talentDetailNoData = $("#talentDetailNoData");
  talentDetailNoData.hide();

  var url = `/interview-schedules?page=${page}&name=${name}&date=${date}`;

  console.log(`url = ${url}`);

  var param = {
    name,
    date,
  };

  $.ajax({
    type: "GET",
    url: url,
    dataType: "json",
    success: function (response) {
      var data = response;
      console.log(data);
      var tbody = $("#talentTbl");
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
              <div class="card-title text-center"><h4>Tidak ada jadwal interview.</h4></div>
            </div>
          </div>
        `;
        tbody.append(`<tr><td colspan="100%">${messageCard}</td></tr>`);
        talentDetailNoData.show();
      } else {
        $.each(data.data, function (index, interviewSchedule) {
          console.log(interviewSchedule);
          var row = $("<tr>");
          let newCell = $("<td>").html(`
            <div class="card rounded-5 interview-list-card my-1 mx-1">
              <div class="rounded-image ml-3">
                <img src="${
                  interviewSchedule.applicant.talent.photo
                    ? interviewSchedule.applicant.talent.photo
                    : "https://cdn-icons-png.flaticon.com/512/3106/3106773.png"
                }" alt="Profile Image"/>
              </div>
              <div class="card-body row">
                <div class="col-8">
                  <h6 class="card-title mb-2 ml-3">${
                    interviewSchedule.applicant.talent.name
                  }</h6>
                  <h6 class="card-text mb-1 ml-3 text-sm-start">${
                    interviewSchedule.position
                  }</h6>
                  <h6 class="card-text mb-1 ml-3 text-sm-start">${
                    interviewSchedule.date
                  }</h6>
                  <h6 class="card-text mb-1 ml-3 text-sm-start">${
                    interviewSchedule.status
                  }</h6>
                </div>
                <div class="col-3 d-flex flex-column ml-2">
                  <button class="btn btn-sm btn-success btn-icon-only mb-1" onclick="process(${
                    interviewSchedule.id
                  })" style="display: ${
            !interviewSchedule.onprocess && interviewSchedule.editable
              ? "inline-block"
              : "none"
          }" data-toggle="tooltip" title="Proses"><i class="fa fa-cogs"></i>
                  </button>
                  <a href="/interview-schedule/update/${
                    interviewSchedule.id
                  }" class="btn btn-sm btn-primary btn-icon-only mb-1"
                    style="display: ${
                      !interviewSchedule.onprocess && interviewSchedule.editable
                        ? "inline-block"
                        : "none"
                    }"
                    data-toggle="tooltip" title="Jadwal ulang"><i class="fa fa-clock"></i>
                  </a>
                  <button class="btn btn-sm btn-warning text-white btn-icon-only mb-1" onclick="cancel(${
                    interviewSchedule.id
                  })"
                      style="display: ${
                        !interviewSchedule.onprocess &&
                        interviewSchedule.editable
                          ? "inline-block"
                          : "none"
                      }"
                      data-toggle="tooltip" title="Batalkan"><i class="fa fa-ban"></i>
                  </button>
                  <button class="btn btn-sm btn-primary btn-icon-only mb-1" onclick="accept(${
                    interviewSchedule.id
                  })"
                      style="display: ${
                        interviewSchedule.onprocess ? "inline-block" : "none"
                      }" data-toggle="tooltip"
                      title="Terima"><i class="fa fa-user-check"></i>
                  </button>
                  <button class="btn btn-sm btn-danger btn-icon-only mb-1" onclick="reject(${
                    interviewSchedule.id
                  })"
                      style="display: ${
                        interviewSchedule.onprocess ? "inline-block" : "none"
                      }" data-toggle="tooltip"
                      title="Tolak"><i class="fa fa-user-times"></i>
                  </button>
                  <a href="/interview-schedule/history/${
                    interviewSchedule.id
                  }" class="btn btn-sm btn-info btn-icon-only mb-1"
                    data-toggle="tooltip" title="Riwayat"><i class="fa fa-history text-white"></i>
                  </a>
                </div>
              </div>    
            </div>
            `);

          row.append(newCell);
          tbody.append(row);
        });
      }

      // Sembunyikan loading overlay setelah selesai
      $("#listTalent").LoadingOverlay("hide");
    },
    error: function () {
      // Sembunyikan loading overlay pada kasus error
      $("#listTalent").LoadingOverlay("hide");
      // Handle error here
    },
  });
}

// Event handler untuk tombol "Previous" dan "Next"
$("#prevPageBtn").click(function () {
  var searchName = $("#searchName").val();
  var searchDate = $("#searchDate").val();
  if (currentPage > 0) {
    displayData(currentPage - 1, searchName, searchDate);
  }
});

$("#nextPageBtn").click(function () {
  var searchName = $("#searchName").val();
  var searchDate = $("#searchDate").val();
  if (currentPage < totalPage - 1) {
    displayData(currentPage + 1, searchName, searchDate);
  }
});

// Panggil displayData untuk menampilkan data pertama kali
displayData(currentPage);

$(function () {
  $('[data-toggle="tooltip"]').tooltip();
});

document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("calendar");

  const dataSrc = () => {
    return new Promise((resolve, reject) => {
      $.ajax({
        type: "GET",
        url: `/api/interview-schedule/recruiter-calendar`,
        dataType: "json",
        success: function (response) {
          resolve(response);
        },
        error: function (error) {
          reject(error);
        },
      });
    });
  };

  const fetchData = async () => {
    try {
      const response = await dataSrc();
      console.log(response);

      const customButton = document.createElement("button");
      customButton.innerText = "Custom Button";
      customButton.addEventListener("click", function () {
        // Tindakan yang akan dilakukan saat tombol custom diklik
        alert("Tombol Custom Diklik!");
      });

      const calendar = new FullCalendar.Calendar(calendarEl, {
        header: {
          center: "customButton",
          right: "today, prev,next",
        },
        plugins: ["dayGrid", "interaction"],
        allDay: false,
        editable: true,
        selectable: true,
        unselectAuto: false,
        displayEventTime: false,
        events: response,
        eventRender: function (info) {
          info.el.addEventListener("contextmenu", function (e) {
            e.preventDefault();
            let existingMenu = document.querySelector(".context-menu");
            existingMenu && existingMenu.remove();

            const eventIndex = response.findIndex(
              (event) => event.id === info.event.id
            );

            console.log(eventIndex);

            document.body.appendChild(menu);
            menu.style.top = e.pageY + "px";
            menu.style.left = e.pageX + "px";
          });
        },

        eventClick: function (info) {
          loadEventDetails(info.event.id);
        },
      });

      calendar.on("select", function (info) {
        console.log(info);
      });

      calendar.render();
    } catch (error) {
      console.error("Terjadi kesalahan dalam permintaan AJAX:", error);
    }
  };

  fetchData();
});

let selectedEventId;

function loadEventDetails(eventId) {
  selectedEventId = eventId;
  $.ajax({
    url: `/api/interview-schedule/${eventId}/detail`,
    method: "GET",
    dataType: "JSON",
    success: (result) => {
      console.log(result);
      // Isi konten modal dengan data yang diterima
      const modalTitle = $("#modal-title");
      const nameElement = $("#name");
      const positionElement = $("#position");
      const dateElement = $("#date");
      const startTimeElement = $("#startTime");
      const endTimeElement = $("#endTime");
      const offlineElement = $("#offline");
      const locationElement = $("#location");
      const linkElement = $("#link");
      const statusElement = $("#status");
      const processButton = $("#processButton");
      const rescheduleButton = $("#rescheduleButton");
      const cancelButton = $("#cancelButton");
      const acceptButton = $("#acceptButton");
      const rejectButton = $("#rejectButton");
      const historyButton = $("#historyButton");

      $("#cancelButton").attr("onclick", `cancel(${result.id})`);
      $("#processButton").attr("onclick", `process(${result.id})`);
      $("#acceptButton").attr("onclick", `accept(${result.id})`);
      $("#rejectButton").attr("onclick", `reject(${result.id})`);

      modalTitle.text("Detail Schedule");
      nameElement.text(result.talent.name);
      positionElement.text(result.position);
      dateElement.text(result.date);
      startTimeElement.text(result.startTime);
      endTimeElement.text(result.endTime);
      offlineElement.text(result.offline ? "Ya" : "Tidak");
      locationElement.html(
        `<span class="fw-bold"> Lokasi : </span> <span>${result.locationAddress}</span>`
      );
      linkElement.html(
        `<span class="fw-bold"> Link : </span> <span>${result.interviewLink}</span>`
      );
      statusElement.text(result.status.name);

      // Tampilkan atau sembunyikan tombol "Reschedule" berdasarkan kondisi
      if (result.offline) {
        $("#location").hide();
        $("#link").show();
      } else {
        $("#location").show();
        $("#link").hide();
      }

      // Process
      if (result.onprocess === false && result.editable === true) {
        processButton.show();
      } else {
        processButton.hide();
      }

      // Reschedule
      if (result.onprocess === false && result.editable === true) {
        rescheduleButton.attr("data-id", result.id);
        rescheduleButton.show();
      } else {
        rescheduleButton.hide();
      }

      // Cancel
      if (result.onprocess === false && result.editable === true) {
        cancelButton.show();
      } else {
        cancelButton.hide();
      }

      // Reject
      if (result.onprocess === true) {
        rejectButton.show();
      } else {
        rejectButton.hide();
      }

      // Accept
      if (result.onprocess === true) {
        acceptButton.show();
      } else {
        acceptButton.hide();
      }

      // History
      historyButton.attr("data-id", result.id);

      // Tampilkan modal
      $("#detailSchedule").modal("show");
    },
  });
}

// Reschedule
$(document).on("click", "#rescheduleButton", function (e) {
  e.preventDefault();
  const eventId = $(this).attr("data-id");
  if (eventId) {
    window.location.href = `/interview-schedule/update/${eventId}`;
  }
});

// History
$(document).on("click", "#historyButton", function (e) {
  e.preventDefault();
  const eventId = $(this).attr("data-id");
  if (eventId) {
    window.location.href = `/interview-schedule/history/${eventId}`;
  }
});

function loading(condition) {
  if (condition) {
    $.LoadingOverlay("show");
  } else {
    $.LoadingOverlay("hide");
  }
}

function cancel(id) {
  Swal.fire({
    title: "Apakah anda ingin membatalkan wawancara ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      var url = `/api/interview-schedule/${id}/cancel`;
      $("#detailSchedule").modal("hide");
      feedback(id, url);
    }
  });
}

function process(id) {
  Swal.fire({
    title: "Apakah anda ingin memproses wawancara ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      var data = JSON.stringify({});
      var url = `/api/interview-schedule/${id}/process`;
      $("#detailSchedule").modal("hide");
      save(id, data, url);
    }
  });
}

function accept(id) {
  Swal.fire({
    title: "Apakah anda ingin menerima talent ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      var url = `/api/interview-schedule/${id}/accept`;
      $("#detailSchedule").modal("hide");
      feedBackWithOnBoardDate(id, url);
    }
  });
}

function reject(id) {
  Swal.fire({
    title: "Apakah anda ingin menolak talent ini?",
    showCancelButton: true,
    confirmButtonText: "Ya",
    cancelButtonText: `Tidak`,
  }).then((result) => {
    if (result.isConfirmed) {
      var url = `/api/interview-schedule/${id}/reject`;
      $("#detailSchedule").modal("hide");
      feedback(id, url);
    }
  });
}

function feedBackWithOnBoardDate(id, url) {
  Swal.fire({
    title: "Notes",
    html:
      '<label for="feedback"></label>' +
      '<input type="text" id="feedback" class="swal2-input" style="width: 18em;" placeholder="Masukkan keterangan">' +
      '<label for="onboard">Tanggal mulai</label>' +
      '<input type="date" id="onboard" class="swal2-input" placeholder="Masukkan tanggal mulai bekerja"  min="' +
      getCurrentDate() +
      '">',
    showCancelButton: true,
    confirmButtonText: "Submit",
    cancelButtonText: "Batal",
    focusConfirm: false,
    preConfirm: () => {
      // Retrieve values from the inputs
      const feedback = document.getElementById("feedback").value;
      const onboard = document.getElementById("onboard").value;

      // Validate inputs
      if (!feedback || !onboard) {
        Swal.showValidationMessage("Tolong isi semuanya");
      } else {
        // You can do something with the valid input values here
        loading(true);
        var data = JSON.stringify({
          feedback: feedback,
          onBoardDate: onboard,
        });
        save(id, data, url);
      }
    },
    allowOutsideClick: () => !Swal.isLoading(),
  });
}

function feedback(id, url) {
  Swal.fire({
    title: "Notes",
    input: "text",
    inputAttributes: {
      autocapitalize: "off",
    },
    showCancelButton: true,
    confirmButtonText: "Kirim",
    cancelButtonText: `Batal`,
    showLoaderOnConfirm: true,
    inputValidator: (value) => {
      if (!value) {
        return "Anda harus menuliskan alasan!";
      }
    },
    preConfirm: (input) => {
      loading(true);
      var data = JSON.stringify({
        feedback: input,
      });
      save(id, data, url);
    },
    allowOutsideClick: () => !Swal.isLoading(),
  }).then((result) => {
    console.log("successs");
  });
}

function save(id, data, url) {
  $.ajax({
    url: url,
    method: "PUT",
    dataType: "JSON",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    data: data,
    success: (result) => {
      // Menyembunyikan overlay loading
      loading(false);

      Swal.fire({
        position: "center",
        icon: "success",
        title: "Data berhasil diperbarui",
        showConfirmButton: false,
        timer: 1500,
      }).then(() => {
        // Memuat ulang halaman
        window.location.href = "/interview-schedule/";
      });
    },
    error: (e) => {
      // Menyembunyikan overlay loading
      loading(false);

      Swal.fire({
        position: "center",
        icon: "error",
        title: "Data gagal diperbarui",
        showConfirmButton: false,
        timer: 1500,
      });
    },
  });
}

$(function () {
  $('[data-toggle="tooltip"]').tooltip();
});
