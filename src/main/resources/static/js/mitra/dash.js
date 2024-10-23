let userTableInitialed = false;
let filterUniversity = null;
let filterMajor = null;
let filterSkill = null;
let filterWork = null;
let filterDegree = null;
let keyword = null;
let page = 0;
let limit = 5;

const debounce = (func, wait, immediate) => {
  let timeout;
  return function () {
    let context = this,
      args = arguments;
    let later = function () {
      timeout = null;
      if (!immediate) func.apply(context, args);
    };
    let callNow = immediate && !timeout;
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
    if (callNow) func.apply(context, args);
  };
};

$(document).ready(($) => {
  initializeDashboardData();
  Chart.register(ChartDataLabels);
  let profileName = readCookie("userName").replaceAll(`"`, "");
  let profilePhoto = readCookie("profilePicture").replaceAll(`"`, "");
  let el = `<img alt="image" id="profilePhoto" src="${profilePhoto}" class="profile-picture mr-auto">
                <div class="d-sm-none d-lg-inline-block" id="profileName">${profileName}</div>`;
  $(".nav-link-user").append(el);

  let select2Selector = [
    "#by-university",
    "#by-major",
    "#by-skill",
    "#by-work",
    "#by-degree",
  ];

  $(select2Selector[0]).on("change", () => {
    filterUniversity = $("#by-university").val();
    if (filterUniversity == "-" || filterUniversity == "") {
      filterUniversity = null;
    }
    page = 0;
    userTableInitialed = false;
    getUser();
  });

  $(select2Selector[1]).on("change", () => {
    filterMajor = $("#by-major").val();
    if (filterMajor == "-" || filterMajor == "") {
      filterMajor = null;
    }
    page = 0;
    userTableInitialed = false;
    getUser();
  });

  $(select2Selector[2]).on("change", () => {
    filterSkill = $("#by-skill").val();
    if (filterSkill == "-" || filterSkill == "") {
      filterSkill = null;
    }
    page = 0;
    userTableInitialed = false;
    getUser();
  });

  $(select2Selector[3]).on("change", () => {
    filterWork = $("#by-work").val();
    if (filterWork == "-" || filterWork == "") {
      filterWork = null;
    }
    page = 0;
    userTableInitialed = false;
    getUser();
  });

  $(select2Selector[4]).on("change", () => {
    filterDegree = $("#by-degree").val();
    if (filterDegree == "-" || filterDegree == "") {
      filterDegree = null;
    }
    page = 0;
    userTableInitialed = false;
    getUser();
  });

  select2Selector.forEach((row) => {
    $(row).hide();
  });

  $.when(getUniversity(), getMajor(), getSkill(), getJob(), getDegree()).done(
    (university, major, skill, job, degree) => {
      $("#loading").hide();
      $("#filter").show();

      university.forEach((row) => {
        let option = `<option value="${row.name}">${row.name}</option>`;
        $("#by-university").append(option);
      });

      major.forEach((row) => {
        let option = `<option value="${row.name}">${row.name}</option>`;
        $("#by-major").append(option);
      });

      skill.forEach((row) => {
        let option = `<option value="${row.name}">${row.name}</option>`;
        $("#by-skill").append(option);
      });

      job.forEach((row) => {
        let option = `<option value="${row.name}">${row.name}</option>`;
        $("#by-work").append(option);
      });
      degree.forEach((row) => {
        let option = `<option value="${row.name}">${row.name}</option>`;
        $("#by-degree").append(option);
      });

      select2Selector.forEach((row, i) => {
        let placeholder = "";
        if (i == 0) {
          placeholder = "Semua Universitas";
        }
        if (i == 1) {
          placeholder = "Semua Jurusan";
        }
        if (i == 2) {
          placeholder = "Semua Skill";
        }
        if (i == 3) {
          placeholder = "Semua Riwayat Pekerjaan";
        }
        if (i == 4) {
          placeholder = "Semua Jenjang";
        }
        $(row).select2({
          width: "100%",
          tags: true,
          dropdownCssClass: "select2-font",
          placeholder: placeholder,
        });
        $(row).fadeIn(2000);
      });
    }
  );
});

$(".user-search").keyup(
  debounce(() => {
    keyword = $(".user-search").val();
    getUser();
    userTableInitialed = false;
  }, 500)
);

const getJob = async () => {
  const result = getData("/api/get-job");
  return result;
};

const getUniversity = async () => {
  const result = getData("/api/get-university");
  return result;
};

const getMajor = async () => {
  const result = getData("/api/get-major");
  return result;
};

const getSkill = async () => {
  const result = getData("/api/get-skill");
  return result;
};
const getDegree = async () => {
  const result = getData("/api/get-degree");
  return result;
};

const initializeDashboardData = () => {
  getUserCount();
  getLastUserUpdated();
  getTopSkill();
  getTopWork();
  getUser();
};

const paginateUserData = (p) => {
  page = p;
  $(".user-pagination").find(".active").removeClass("active");
  $(`.paginate-${p}`).addClass("active");
  getUser();
};

const getUserCount = () => {
  getData("/api/get-count-user").then((response) => {
    $(".total-user").text(response.totalUser);
    $(".total-cv").text(response.totalSubmitedCv);
  });
};

const viewMoreUser = (size) => {
  getLastUserUpdated(size);
  $("#showMore").fadeOut();
};

let paginationX = 0;

const scrollUpPagination = () => {
  $(document)
    .find(".pagination-scroll")
    .animate({
      scrollLeft: paginationX + 100,
    });
  paginationX += 100;
};

const scrollDownPagination = () => {
  $(document)
    .find(".pagination-scroll")
    .animate({
      scrollLeft: paginationX - 100,
    });
  paginationX -= 100;
};

const createPagination = (response) => {
  $(".user-pagination").empty();
  let pages = Math.ceil(response.totalRecord / limit);
  let el = "";
  if (pages > 9) {
    el = `<li class="page-item">
                <a class="page-link" href="javascript:void(0)" onclick="scrollDownPagination()" tabindex="-1"><i class="fas fa-chevron-left"></i></a>
              </li>`;
    el += `<div class="pagination-scroll">`;
  }
  for (let i = 0; i < pages; i++) {
    el += `<li class="page-item ${
      page == i ? "active" : ""
    } paginate-${i}"><a class="page-link" href="javascript:void(0)" onclick="paginateUserData(${i})">${
      i + 1
    }</a></li>`;
  }

  if (pages > 9) {
    el += `</div>`;
    el += `<li class="page-item">
             <a class="page-link" href="javascript:void(0)" onclick="scrollUpPagination()" ><i class="fas fa-chevron-right"></i></a>
           </li>`;
  }

  $(".user-pagination").append(el);
};

// function adjustColumnWidths() {
//     const thElements = document.querySelectorAll('#user-table thead th');
//     const tdElements = document.querySelectorAll('#user-table tbody tr:first-child td');

//     if (thElements.length !== tdElements.length) {
//         return;
//     }

//     tdElements.forEach((td, index) => {
//         const tdWidth = td.clientWidth;
//         thElements[index].style.width = `${tdWidth}px`;
//     });
// }

const getUser = () => {
  const data = {
    limit,
    page,
    university: filterUniversity == null ? null : filterUniversity.join("|"),
    major: filterMajor == null ? null : filterMajor.join("|"),
    degree: filterDegree == null ? null : filterDegree.join("|"),
    job: filterWork == null ? null : filterWork.join("|"),
    skill: filterSkill == null ? null : filterSkill.join("|"),
    name: keyword == null || keyword == "" ? null : keyword,
  };

  $("#user-table tbody").fadeOut(400);
  $("#user-table tbody").empty();
  $("#user-table tbody").fadeIn(400);
  $("#user-table tbody").append(
    `<tr><td colspan="8" class="text-center"><b>Mohon tunggu...</b></td></tr>`
  );

  setTimeout(() => {
    postData("/api/get-all-user-datatable", JSON.stringify(data))
      .then((response) => {
        createPagination(response);
        const tableBody = buildTableBody(response.data);
        $("#user-table tbody").empty().append(tableBody);
      })
      .catch((err) => {
        console.log(err);
      });
  }, 0);
};

const buildTableBody = (data) => {
  if (data.length === 0) {
    return `<tr><td colspan="2" class="text-center"><b>Tidak ada data ditemukan.</b></td></tr>`;
  }

  let tableBody = "";
  let count = page * 5 + 1;

  data.forEach((row) => {
    const skills = row.skill.slice(0, 2).join(", ");

    let workAssignment = "";
    row.workAssignment.slice(0, 3).forEach((work) => {
      workAssignment += `
                <div class="container pb-2">
                    <div class="row"><strong>${work.position}</strong></div>
                    <div class="row">at ${work.companyName}</div>
                    <div class="row">${work.startDate} - ${
        work.endDate ?? "Present"
      }</div>
                </div>
            `;
    });

    tableBody += `<tr>`;
    tableBody += `<td>${count++}</td>`;
    tableBody += `<td>${row.name}</td>`;
    tableBody += `<td>${skills + `...`}</td>`;
    tableBody += `<td>${workAssignment}</td>`;
    tableBody += `<td>${row.currentEducation}</td>`;
    tableBody += `<td>${row.currentDegree}</td>`;
    tableBody += `<td>${row.updateAt}</td>`;
    tableBody += `<td>
            <a href="javascript:void(0)" onclick="previewCv('${row.userUrl}')" class="btn btn-primary btn-sm btn-block btn-round">Preview</a>
            <button type="button" class="btn btn-danger btn-sm btn-block btn-round dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Download
            </button>
            <div class="dropdown-menu">
                <a class="dropdown-item" href="/download-cv/${row.userUrl}" onclick="downloadCv('pdf')">Download as PDF</a>
                <a class="dropdown-item" href="/download-cv/docx/${row.userUrl}" onclick="downloadCv('word')">Download as Word</a>
            </div>
        </td>`;
    tableBody += `</tr>`;
  });

  return tableBody;
};

const getLastUserUpdated = (size = 5) => {
  // Tabel di menu Dashboard pada hak akses Admin
  $("#last-user-table").fadeOut(400, () => {
    getData("/api/get-last-updated-user?size=" + size).then((response) => {
      $("#last-user-table").DataTable({
        language: {
          url: "//cdn.datatables.net/plug-ins/1.11.3/i18n/id.json",
        },
        searching: false,
        paging: false,
        info: false,
        destroy: true,
        data: response,
        columns: [
          {
            data: "id",
            render: (data, type, row, meta) => {
              return meta.row + 1;
            },
            orderable: false,
          },
          {
            data: "name",
          },
          {
            data: "currentEducation",
          },
          {
            data: "currentDegree",
          },

          {
            data: "workAssignment",
            render: (data, type, row) => {
              let workAssignment = "";
              if (Array.isArray(row.workAssignment)) {
                row.workAssignment.slice(0, 3).forEach((work) => {
                  workAssignment += `
                                        <div class="container pb-2">
                                        <div class="row"><strong>${
                                          work.projectSpecification
                                        }</strong></div>
                                        <div class="row">at ${
                                          work.companyName
                                        }</div>
                                        <div class="row">${work.startDate} - ${
                    work.endDate ?? "Present"
                  }</div>
                                        </div>
                                        `;
                });
              }
              return workAssignment;
            },
          },
          {
            data: "updateAt",
          },
          {
            data: "id",
            render: (data, type, row, meta) => {
              const preview = `
                                        <a href="javascript:void(0)" onclick="previewCv('${row.userUrl}')" class="btn btn-primary btn-sm btn-block mb-2 btn-round">
                                            Preview
                                        </a>`;
              // const download = `<a href="/download-cv/${row.userUrl}" class="btn btn-danger btn-sm btn-block btn-round" onclick="downloadCv()">
              //                 Download
              //             </a>`;
              const download = `
                                                <button type="button" class="btn btn-danger btn-sm btn-block btn-round dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                    Download
                                                </button>
                                                <div class="dropdown-menu">
                                                    <a class="dropdown-item" href="/download-cv/${row.userUrl}" onclick="downloadCv('pdf')">Download as PDF</a>
                                                    <a class="dropdown-item" href="/download-cv/docx/${row.userUrl}" onclick="downloadCv('word')">Download as Word</a>
                                                </div>
                                            `;
              return preview + download;
            },
            orderable: false,
          },
        ],
      });
    });
  });
  $("#last-user-table").fadeIn(400);
};

const getTopSkill = () => {
  getData("/api/get-top-skill").then((response) => {
    let labels = response.map((res) => {
      return res.name;
    });

    let values = response.map((res) => {
      return res.count;
    });

    let total = 0;

    values.forEach((row) => (total += row));

    let el = $("#skill-chart");
    let myChart = new Chart(el, {
      type: "pie",
      data: {
        labels: labels,
        datasets: [
          {
            label: "",
            data: values,
            backgroundColor: [
              "#E0FFCD",
              "#D4D9FF",
              "#FFB0B0",
              "#FEFFBA",
              "#BAABDA",
              "#D3DEDC",
              "#92A9BD",
              "#FFEFEF",
              "#D77FA1",
              "#D6E5FA",
            ],
            borderColor: [
              "#E0FFCD",
              "#D4D9FF",
              "#FFB0B0",
              "#FEFFBA",
              "#BAABDA",
              "#D3DEDC",
              "#92A9BD",
              "#FFEFEF",
              "#D77FA1",
              "#D6E5FA",
            ],
            borderWidth: 1,
          },
        ],
      },
      options: {
        //cutoutPercentage: 40,
        responsive: true,
        plugins: {
          datalabels: {
            formatter: (value) => {
              let result = (value / total) * 100;
              return result.toFixed(2) + "%";
            },
          },
        },
      },
    });
  });
};

const getTopWork = () => {
  getData("/api/get-top-work-experience").then((response) => {
    let workTotal = 0;

    let backgroundColor = [
      "#E0FFCD",
      "#D4D9FF",
      "#FFB0B0",
      "#FEFFBA",
      "#BAABDA",
      "#D3DEDC",
      "#92A9BD",
      "#FFEFEF",
      "#D77FA1",
      "#D6E5FA",
    ];

    response.map((res) => {
      workTotal += res.count;
    });

    let html = "";
    let iterator = 0;

    response.forEach((res) => {
      let progress = workTotal == 0 ? 0 : (res.count / workTotal) * 100;

      html += `<div class="progress-container mb-3">
                        <div class="d-flex justify-content-between">
                            <h6>${res.name}</h6>
                            <h6>${res.count} (${progress.toFixed(2)}%)</h6>
                        </div>
                         <div class="progress">
                            <div class="progress-bar" role="progressbar" style="width: ${progress}%; background-color: ${
        backgroundColor[iterator]
      }" aria-valuenow="${progress.toFixed(
        2
      )}" aria-valuemin="0" aria-valuemax="100"></div>
                         </div>
                     </div>`;
      iterator += 1;
    });

    iterator = 0;

    $("#work-chart").append(html).fadeIn(2000);
  });
};

const readCookie = (name) => {
  let nameEQ = name + "=";
  let ca = document.cookie.split(";");
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == " ") c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
  }
  return null;
};

const previewCv = (userUrl) => {
  $("#modalPreview").modal("show");
  $("#loader").show();
  $("#modalPreview")
    .find("iframe")
    .attr("src", `/share/${userUrl}?hideHeaderImage=true`);
  $("#modalPreview")
    .find("iframe")
    .on("load", function () {
      $("#loader").hide();
      $("#modalPreview").find("iframe").show();
    });
};

$("#modalPreview").on("hidden.bs.modal", function (e) {
  $("#modalPreview").find("iframe").hide().attr("src", "");
});
