let currentPage = 0; // Halaman saat ini
let totalPage = 1;

function displayPaginationLinks(currentPage, totalPage) {
  let i;
  const pagination = $("#pagination");
  pagination.empty();

  const numLinks = 3; // Jumlah tautan pagination sebelum dan sesudah currentPage

  // Tautan "First Page" (sembunyikan jika di halaman pertama)
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(0)"><<</a></li>`
    );
  }

  // Tautan "Previous"
  if (currentPage > 0) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        currentPage - 1
      })"><</a></li>`
    );
  }

  // Tampilkan tautan sebelum currentPage
  for (i = currentPage - numLinks; i < currentPage; i++) {
    if (i >= 0) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i})">${
          i + 1
        }</a></li>`
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
  for (i = currentPage + 1; i <= currentPage + numLinks; i++) {
    if (i < totalPage) {
      pagination.append(
        `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i})">${
          i + 1
        }</a></li>`
      );
    }
  }

  // Tautan "Next"
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        currentPage + 1
      })">></a></li>`
    );
  }

  // Tautan "Last Page" (sembunyikan jika di halaman terakhir)
  if (currentPage < totalPage - 1) {
    pagination.append(
      `<li class="page-item"><a class="page-link" href="#" onclick="displayData(${
        totalPage - 1
      })">>></a></li>`
    );
  }
}

// Fungsi untuk mengupdate informasi pagination
function updatePaginationInfo(currentPage, totalPage, data) {
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

  displayPaginationLinks(currentPage, totalPage);
}

// Event handler untuk input pencarian dan pemantauan perubahan date
$("#searchInput, #date").on("input change", function () {
  const searchInput = $("#searchInput").val();
  const dateInput = $("#date").val();
  displayData(0, searchInput, dateInput);
});

// Fungsi untuk menampilkan data berdasarkan halaman
function displayData(page, name = "", date) {
  let url = `/api/interview-schedule/recruiter-accepted-talent?page=${page}&name=${name}`;
  if (date !== undefined && date != null && date !== "") {
    url = `/api/interview-schedule/recruiter-accepted-talent?page=${page}&name=${name}&date=${date}`;
  }
  $.ajax({
    type: "GET",
    url: url,
    dataType: "json",
    success: function (response) {
      const data = response;
      const tbody = $("#employeeTbl");
      totalPage =
        data.pageData.total === 10
          ? data.pageData.lastPage
          : data.pageData.lastPage + 1;
      currentPage = page;
      updatePaginationInfo(currentPage, totalPage, data);
      const startNum = page * data.pageData.perPage + 1;
      tbody.empty();

      $.each(data.interviewSchedules, function (index, interviewSchedule) {
        const row = $("<tr>");
        row.append($("<td class='text-center'>").text(index + startNum));
        row.append(
          $("<td>").text(interviewSchedule.talent.name)
        );
        row.append(
          $("<td>").text(interviewSchedule.position)
        );
        row.append(
          $("<td class='text-center'>").text(interviewSchedule.onBoardDate)
        );
        row.append(
          $("<td class='text-center'>").html(
            `<a class="btn btn-primary" href="/interview-schedule/history/${interviewSchedule.id}" style="margin-top: 18"><i class="fa fa-history"></i></a>`
          )
        );
        tbody.append(row);
      });
    },
  });
}

// Event handler untuk tombol "Previous" dan "Next"
$("#prevPageBtn").click(function () {
  if (currentPage > 0) {
    displayData(currentPage - 1);
  }
});

$("#nextPageBtn").click(function () {
  if (currentPage < totalPage - 1) {
    displayData(currentPage + 1);
  }
});

// Panggil displayData untuk menampilkan data pertama kali
displayData(currentPage);
