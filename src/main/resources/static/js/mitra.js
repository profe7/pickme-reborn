$(document).ready(function () {
  displayData();
  displayDataJob(currentPage);
});

var currentPage = 0;
var totalPage = 1;

function displayPaginationLinks(currentPage, totalPage, param) {
  var pagination = $("#pagination");
  pagination.empty();

  var numLinks = 2;

  // First Page
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayDataJob(0,
      '${param.timeInterval}')"><<</a></li>`
    );
  }

  // Previous
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayDataJob(${
        currentPage - 1
      }, '${param.timeInterval}')"><</a></li>`
    );
  }

  // Before currentPage
  for (var i = currentPage - numLinks; i < currentPage; i++) {
    if (i >= 0) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayDataJob(${i}, 
          '${param.timeInterval}')">${i + 1}</a></li>`
      );
    }
  }

  // Current Page
  pagination.append(
    `<li class="page-item active"><span class="page-link">${
      currentPage + 1
    }</span></li>`
  );

  // After current page
  for (var i = currentPage + 1; i <= currentPage + numLinks; i++) {
    if (i < totalPage) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayDataJob(${i}, 
          '${param.timeInterval}')">${i + 1}</a></li>`
      );
    }
  }

  // Next
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayDataJob(${
        currentPage + 1
      }, '${param.timeInterval}')">></a></li>`
    );
  }

  // Last page
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayDataJob(${
        totalPage - 1
      }, '${param.timeInterval}')">>></a></li>`
    );
  }
}

function updatePaginationInfo(currentPage, totalPage, data, param) {
  $("#paginationInfo").text(
    `Halaman ${currentPage + 1} dari ${totalPage}, Total Data: ${
      data.pageData.total
    }`
  );

  if (currentPage > 0) {
    $("#firstPageBtn").show();
    $("#prevPageBtn").show();
  } else {
    $("#firstPageBtn").hide();
    $("#prevPageBtn").hide();
  }

  if (currentPage < totalPage - 1) {
    $("#lastPageBtn").show();
    $("#nextPageBtn").show();
  } else {
    $("#lastPageBtn").hide();
    $("#nextPageBtn").hide();
  }

  displayPaginationLinks(currentPage, totalPage, param);
}

function displayData() {
  $.ajax({
    type: "GET",
    url: `/api/talent/total-for-mitra`,
    dataType: "json",
    success: function (response) {
      const statuses = [];
      const totals = [];
      const colors = [];

      response.forEach((item) => {
        statuses.push(item.status);
        totals.push(item.total);
        colors.push(item.color);
      });

      myTalentChart(statuses, totals, colors);
    },
  });
}

function myTalentChart(statuses, totals, colors) {
  const ctx = document.getElementById("myTalentChart");

  new Chart(ctx, {
    type: "doughnut",
    data: {
      labels: statuses,
      datasets: [
        {
          data: totals,
          backgroundColor: colors,
          hoverOffset: 4,
        },
      ],
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          display: false,
          position: "bottom",
          labels: {
            boxWidth: 10,
            boxHeight: 10,
            usePointStyle: true,
            font: {
              size: 15,
            },
            generateLabels: function (chart) {
              const data = chart.data;
              if (data.labels.length && data.datasets.length) {
                return data.labels.map((label, index) => {
                  const value = data.datasets[0].data[index];
                  return {
                    text: `${label}\n${value}`, // Menampilkan label dan jumlah
                    fillStyle: data.datasets[0].backgroundColor[index],
                    hidden: isNaN(data.datasets[0].data[index]),
                    index: index,
                    pointStyle: "rectRounded",
                  };
                });
              }
              return [];
            },
          },
        },
        title: {
          display: true,
          // text: "My Talent",
          align: "start",
          font: {
            size: 30,
          },
        },
      },
      cutout: "65%",
    },
  });
}

$("#filterMonthly, #filterWeekly, #filterToday, #filterAll").on(
  "click",
  function () {
    if ($("#filterAll").hasClass("active")) {
      $("#filterAll").removeClass("active");
    } else {
      $("#filterAll").addClass("active");
    }
    $("#filterMonthly, #filterWeekly, #filterToday, #filterAll").removeClass(
      "active"
    );
    $(this).addClass("active");

    var timeInterval = getTimeInterval();
    displayDataJob(0, timeInterval);
  }
);

function getTimeInterval() {
  if ($("#filterMonthly").hasClass("active")) {
    return "bulan";
  } else if ($("#filterWeekly").hasClass("active")) {
    return "minggu";
  } else if ($("#filterToday").hasClass("active")) {
    return "hari";
  }
  return "";
}

function displayDataJob(page, timeInterval = "") {
  var url = `/api/job/applicants?page=${page}`;

  if (
    timeInterval === "hari" ||
    timeInterval === "minggu" ||
    timeInterval === "bulan"
  ) {
    url += `&timeInterval=${timeInterval}`;
  }

  var param = {
    timeInterval,
  };

  $.ajax({
    type: "GET",
    url: url,
    dataType: "json",
    success: function (response) {
      var data = response;
      var tbody = $("#jobList");
      totalPage = data.pageData.lastPage + 1;
      currentPage = page;

      // Update informasi pagination
      updatePaginationInfo(currentPage, totalPage, data, param);

      tbody.empty();

      data.totalApplicantByJob.forEach(function (jobApplicant) {
        if (jobApplicant && jobApplicant.job) {
          var row = $("<tr></tr>");

          var jobTitle = $("<td class='text-center'></td>").text(
            jobApplicant.job.title
          );
          var jobPosition = $("<td class='text-center'></td>").text(
            jobApplicant.job.position
          );
          var jobDescription = $("<td></td>").html(
            jobApplicant.job.description
          );
          var totalNominee = $("<td class='text-center'></td>").text(
            jobApplicant.totalNominee
          );
          var applyButton = $("<td class='text-center'></td>").html(
            '<a type="button" class="btn btn-apply" style="width: 5em;" href="/job/' +
              jobApplicant.job.id +
              '">Apply</a>'
          );

          row.append(
            jobTitle,
            jobPosition,
            jobDescription,
            totalNominee,
            applyButton
          );
          tbody.append(row);
        } else {
          console.error("Data jobApplicant tidak lengkap:", jobApplicant);
        }
      });
    },
  });
}

$("#prevPageBtn").click(function () {
  if (currentPage > 0) {
    displayDataJob(currentPage - 1);
  }
});

$("#nextPageBtn").click(function () {
  if (currentPage < totalPage - 1) {
    displayDataJob(currentPage + 1);
  }
});

$(function () {
  $('[data-toggle="tooltip"]').tooltip();
});

document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("calendar");

  const dataSrc = () => {
    return new Promise((resolve, reject) => {
      $.ajax({
        type: "GET",
        url: `/api/interview-schedule/mitra-calendar`,
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
      // console.log(response);

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
      const historyButton = $("#historyButton");

      modalTitle.text("Detail Schedule");
      nameElement.text(result.talent.name);
      positionElement.text(result.position);
      dateElement.text(result.date);
      startTimeElement.text(result.startTime);
      endTimeElement.text(result.endTime);
      offlineElement.text(result.offline ? "Yes" : "No");
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

      // History
      historyButton.attr("data-id", result.id);

      // Tampilkan modal
      $("#detailSchedule").modal("show");
    },
  });
}

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

$(document).ready(function () {
  $("#rejectModal").on("show.bs.modal", function (event) {
    var modal = $(this);

    // Mengirimkan permintaan AJAX ke endpoint backend untuk mendapatkan semua data
    $.ajax({
      type: "GET",
      url: "api/interview-schedule-history/listHistory",
      dataType: "json",
      success: function (response) {
        $("#tableBody").empty();

        // Loop melalui data yang diterima dan membuat kartu untuk setiap data
        $.each(response, function (index, data) {
          var created_at = moment(data.created_at);

          // Memformat tanggal dan waktu menggunakan Moment.js
          var formattedDateTime = created_at.format("DD MMMM YYYY HH:mm:ss");
          var tableRow = `
            <tr>
              <th scope="row">${index + 1}</th>
              <td>${data.talentName}</td>
              <td>${data.feedback}</td>
              <td>${formattedDateTime}</td>
              <td>${data.instituteName}</td>
            </tr>
          `;

          // Menambahkan kartu ke dalam container kartu
          $("#tableBody").append(tableRow);
        });

        // Menampilkan modal setelah semua kartu ditambahkan
        modal.modal("show");
      },
      error: function (xhr, status, error) {
        console.error("Terjadi kesalahan saat mengambil data:", error);
      },
    });
  });
});

$(document).ready(function () {
  $("#acceptModal").on("show.bs.modal", function (event) {
    var modal = $(this);

    // Mengirimkan permintaan AJAX ke endpoint backend untuk mendapatkan semua data
    $.ajax({
      type: "GET",
      url: "api/interview-schedule-history/listHistory2",
      dataType: "json",
      success: function (response) {
        $("#tableBody2").empty();

        // Loop melalui data yang diterima dan membuat kartu untuk setiap data
        $.each(response, function (index, data) {
          // var formattedDateTime = moment(data.onBoardDate).format('DD MMMM YYYY');
          var tableRow = `
            <tr>
              <th scope="row">${index + 1}</th>
              <td>${data.talentName}</td>
              <td>${data.instituteName}</td>
              <td>${data.onBoardDate}</td>
            </tr>
          `;

          // Menambahkan kartu ke dalam container kartu
          $("#tableBody2").append(tableRow);
        });

        // Menampilkan modal setelah semua kartu ditambahkan
        modal.modal("show");
      },
      error: function (xhr, status, error) {
        console.error("Terjadi kesalahan saat mengambil data:", error);
      },
    });
  });
});

function reloadPage() {
  location.reload();
}

$(document).ready(function () {
  // Ensure all modals have the correct functionality
  $("#rejectModal").on("hidden.bs.modal", function () {
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
  });
  $("#acceptModal").on("hidden.bs.modal", function () {
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
  });
  $("#detailSchedule").on("hidden.bs.modal", function () {
    $("body").removeClass("modal-open");
    $(".modal-backdrop").remove();
  });
});
