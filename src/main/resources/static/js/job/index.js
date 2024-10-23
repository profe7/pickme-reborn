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
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(0, '${param.title}',
      '${param.position}','${param.expiredDate}',
      '${param.timeInterval}')"><<</a></li>`
    );
  }

  // Tautan "Previous"
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        currentPage - 1
      }, '${param.title}','${param.position}','${param.expiredDate}', '${
        param.timeInterval
      }')"><</a></li>`
    );
  }

  // Tampilkan tautan sebelum currentPage
  for (var i = currentPage - numLinks; i < currentPage; i++) {
    if (i >= 0) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i}, '${
          param.title
        }','${param.position}','${param.expiredDate}','${
          param.timeInterval
        }')">${i + 1}</a></li>`
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
          param.title
        }','${param.position}','${param.expiredDate}','${
          param.timeInterval
        }')">${i + 1}</a></li>`
      );
    }
  }

  // Tautan "Next"
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        currentPage + 1
      }, '${param.title}','${param.position}','${param.expiredDate}', '${
        param.timeInterval
      }')">></a></li>`
    );
  }

  // Tautan "Last Page" (sembunyikan jika di halaman terakhir)
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        totalPage - 1
      }, '${param.title}','${param.position}','${param.expiredDate}', '${
        param.timeInterval
      }')">>></a></li>`
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

$("#searchTitle, #searchPosition, #searchDateExpired").on("input", function () {
  var searchPosition = $("#searchPosition").val();
  var searchDateExpired = $("#searchDateExpired").val();
  var searchTitle = $("#searchTitle").val();
  var timeInterval = getTimeInterval();
  displayData(0, searchTitle, searchPosition, searchDateExpired, timeInterval);
});

$("#filter24h, #filter1w, #filter1m, #filterAnytime").on("change", function () {
  var searchPosition = $("#searchPosition").val();
  var searchDateExpired = $("#searchDateExpired").val();
  var searchTitle = $("#searchTitle").val();
  var timeInterval = getTimeInterval();
  displayData(0, searchTitle, searchPosition, searchDateExpired, timeInterval);
});

function getTimeInterval() {
  if ($("#filter24h").prop("checked")) {
    return "hari";
  } else if ($("#filter1w").prop("checked")) {
    return "minggu";
  } else if ($("#filter1m").prop("checked")) {
    return "bulan";
  } else if ($("#filterAnytime").prop("checked")) {
    return "anytime";
  }
  return "";
}

function apply(jobId) {
  var data = JSON.stringify({ id: jobId });
  $.ajax({
    type: "POST",
    url: `/api/job/encrypt`,
    data: data,
    dataType: "text",
    beforeSend: addCsrfToken(),
    contentType: "application/json",
    success: function (response) {
      window.location.href = `/applicant/create?token=${response}`;
    },
  });
}

// Fungsi untuk menampilkan data berdasarkan halaman
function displayData(
  page,
  title = "",
  position = "",
  expiredDate = "",
  timeInterval = ""
) {
  $("#detailCard").LoadingOverlay("show");
  $("#listJob").LoadingOverlay("show");
  let jobDetailNoData = $("#jobDetailNoData");
  jobDetailNoData.hide();
  let root = $("#detailCard");
  root.LoadingOverlay("show");
  var jobDetail = $("#jobDetail");
  var url = `/api/job?page=${page}`;

  if (title) {
    url += `&title=${title}`;
  }

  if (position) {
    url += `&position=${position}`;
  }

  if (expiredDate) {
    url += `&expiredDate=${expiredDate}`;
  }

  if (
    timeInterval === "hari" ||
    timeInterval === "minggu" ||
    timeInterval === "bulan"
  ) {
    url += `&timeInterval=${timeInterval}`;
  }

  var param = {
    title,
    position,
    expiredDate,
    timeInterval,
  };

  $.ajax({
    type: "GET",
    url: url,
    dataType: "json",
    success: function (response) {
      var data = response;
      var tbody = $("#jobDetail");
      totalPage = data.pageData.lastPage + 1;
      currentPage = page;
      updatePaginationInfo(currentPage, totalPage, data, param);
      var startNum = page * data.pageData.perPage + 1;
      tbody.empty();

      var totalJobs = data.data.length;
      var columnsPerRow = 2;
      var totalRows = Math.ceil(totalJobs / columnsPerRow);

      if (totalJobs === 0) {
        jobDetail.hide();
        jobDetailNoData.show();
      } else {
        jobDetail.empty();

        data.data.sort(function (a, b) {
          return new Date(b.created_at) - new Date(a.created_at);
        });

        $.each(new Array(totalRows), function (i) {
          // Menambahkan elemen div dengan kelas "row"
          var row = $('<div class="row">');

          // Loop untuk setiap kolom dalam baris
          $.each(new Array(columnsPerRow), function (j) {
            // Menghitung indeks pekerjaan dalam data
            var jobIndex = i * columnsPerRow + j;

            // Memeriksa apakah indeks pekerjaan valid
            if (jobIndex < totalJobs) {
              // Mendapatkan pekerjaan berdasarkan indeks
              var job = data.data[jobIndex];

              // Menghitung selisih hari antara tanggal pembuatan dan tanggal saat ini
              var created_atDate = new Date(job.created_at);
              var currentDate = new Date();
              var daysDiff = Math.floor(
                (currentDate - created_atDate) / (1000 * 60 * 60 * 24)
              );

              // Ensure daysDiff is at least 0
              daysDiff = Math.max(daysDiff, 0);

              // Check the time interval and add the cardJob and HTML markup together
              if (timeInterval === "hari" && daysDiff <= 1) {
                row.append(cardJob(job, daysDiff));
              } else if (timeInterval === "minggu" && daysDiff <= 7) {
                row.append(cardJob(job, daysDiff));
              } else if (timeInterval === "bulan" && daysDiff <= 30) {
                row.append(cardJob(job, daysDiff));
              } else {
                row.append(cardJob(job, daysDiff));
              }
            }
          });

          // Menambahkan baris ke elemen jobDetail
          jobDetail.append(row);
        });

        // Menampilkan jobDetail setelah selesai semua iterasi
        jobDetail.show();
      }

      // Sembunyikan loading overlay setelah selesai
      $("#detailCard").LoadingOverlay("hide", true);
      $("#listJob").LoadingOverlay("hide");
    },
    error: function () {
      // Sembunyikan loading overlay pada kasus error
      $("#detailCard").LoadingOverlay("hide", true);
      $("#listJob").LoadingOverlay("hide");
      // Handle error here
    },
  });
}

function cardJob(job, daysDiff) {
  // Menambahkan elemen div dengan kelas "row"
  return `
    <div class="col-md-6 mb-2">
      <div class="card rounded-3" style="width: 100%; height: 100%;">
        <div class="card-body" style="display: flex; flex-direction: column;">
          <div class="card-title text-left">
            <p class="fw-bold">${job.title}</p>
          </div>
          <p class="job-info fw-normal text-primary">${job.position}</p>                        
          <p class="job-info fw-normal">${job.expiredDate}</p>
          <p class="job-info fw-normal">${job.description}</p>
          <div class="mt-auto">
            <hr/>
            <div class="row mt-3 mx-auto">                        
              <a href="/job/${job.id}" class="btn btn-success col-md-3" id="btnDetail" style="font-size: 14px;">Detail</a>
              <p class="job-info fw-normal col-md-9 mt-2" style="opacity: 0.6;">Dibuat ${daysDiff} hari yang lalu</p>                        
            </div>
          </div>                                                                 
        </div>                  
      </div>
    </div>                
  `;
}

// Event handler untuk tombol "Previous" dan "Next"
$("#prevPageBtn").click(function () {
  var searchTitle = $("#searchTitle").val();
  var searchPosition = $("#searchPosition").val();
  var searchDate = $("#searchDate").val();
  if (currentPage > 0) {
    displayData(currentPage - 1, searchTitle, searchPosition, searchDate);
  }
});

$("#nextPageBtn").click(function () {
  var searchTitle = $("#searchTitle").val();
  var searchPosition = $("#searchPosition").val();
  var searchDate = $("#searchDate").val();
  if (currentPage < totalPage - 1) {
    displayData(currentPage + 1, searchTitle, searchPosition, searchDate);
  }
});

// Panggil displayData untuk menampilkan data pertama kali
displayData(currentPage);

$(function () {
  $('[data-toggle="tooltip"]').tooltip();
});
