$(document).ready(function () {
     $(".select2").select2();
});

const baseUrl = "https://dev.cv-me.metrodataacademy.id/";
// const baseUrl = "http://localhost:8087/";

// function sendRequest(mitraId, mitraName) {
//   // Mengambil nilai dari variabel 'name'
//   var mitraName = mitraName;

//   // Menambahkan "HH" di depan nilai 'mitraId'
//   var mitraId = "HH" + mitraId;

//   // Mengambil data dari sessionStorage
//   var sessionData = {
//     mitraName: mitraName,
//     mitraId: mitraId,
//   };

//   // Mengambil data dari cookie
//   var cookies = document.cookie.split(";").reduce(function (acc, cookie) {
//     var parts = cookie.trim().split("=");
//     acc[parts[0]] = parts[1];
//     return acc;
//   }, {});

//   var requestData = {
//     mitraId: mitraId,
//     mitraName: mitraName,
//     sessionData: sessionData,
//     cookies: cookies,
//   };

//   // Menyusun URL
//   var url = buildUrl(mitraId, mitraName);

//   // Menambahkan requestData dalam payload permintaan AJAX
//   $.ajax({
//     url: url,
//     method: "GET",
//     data: requestData,
//     success: function (response) {
//       // Menyimpan data ke sessionStorage
//       sessionStorage.setItem("mitraName", response.hhName);
//       sessionStorage.setItem("mitraId", response.hhId);

//       // Menyimpan data ke cookies (jika ada respons dari server)
//       if (response.cookies) {
//         document.cookie = `userName=${response.cookies.hhName}; ;path=/`;
//         document.cookie = `profilePicture=${response.cookies.profilePicture}; ;path=/`;
//       }

//       // Menunggu 500ms sebelum membuka tab baru
//       setTimeout(function () {
//         var newTab = window.open(baseUrl + response.redirect, "_blank");
//         newTab.focus();
//       }, 500);

//       console.log(response);
//     },
//     error: function (xhr, status, error) {
//       console.error("Error", error);
//     },
//   });
// }

// // menyusun Url
// function buildUrl(mitraId, mitraName) {
//   // Mengganti spasi dengan %20 pada parameter 'mitraName'
//   mitraName = encodeURIComponent(mitraName);

//   return `${baseUrl}/mitra/${mitraId}/${mitraName}`;
// }

// ajaxRequest.js

// Fungsi untuk melakukan permintaan Ajax
function fetchData(userId, username, password) {
     // Menggunakan btoa untuk membuat token basis64 dari username dan password
     var basicAuth = "Basic " + btoa(username + ":" + password);

     // Membuat header dengan Authorization
     var headers = {
          Authorization: basicAuth,
     };

     // Melakukan permintaan Ajax
     $.ajax({
          url: `${baseUrl}mitra/ + ${userId}`,
          type: "GET",
          headers: headers,
          success: function (response) {
               // Logika untuk menangani respons dari server
               console.log(response);
          },
          error: function (error) {
               // Logika untuk menangani kesalahan
               console.error(error);
          },
     });
}

var currentPage = 0; // Halaman saat ini
var totalPage = 1;
let inviteButton = $(".invite-button");

function displayPaginationLinks(currentPage, totalPage, param) {
     var pagination = $("#pagination");
     pagination.empty();

     var numLinks = 2; // Jumlah tautan pagination sebelum dan sesudah currentPage

     // Tautan "First Page" (sembunyikan jika di halaman pertama)
     if (currentPage > 0) {
          pagination.append(`<li class="page-item"><a class="page-link" href="#" onclick="displayData(0, '${param.job}','${param.skill}','1')"><<</a></li>`);
     }

     // Tautan "Previous"
     if (currentPage > 0) {
          pagination.append(`<li class="page-item"><a class="page-link" href="#" onclick="displayData(${currentPage - 1}, '${param.job}','${param.skill}')"><</a></li>`);
     }

     // Tampilkan tautan sebelum currentPage
     for (var i = currentPage - numLinks; i < currentPage; i++) {
          if (i >= 0) {
               pagination.append(`<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i}, '${param.job}','${param.skill}')">${i + 1}</a></li>`);
          }
     }

     // Tautan currentPage
     pagination.append(`<li class="page-item active"><span class="page-link">${currentPage + 1}</span></li>`);

     // Tampilkan tautan sesudah currentPage
     for (var i = currentPage + 1; i <= currentPage + numLinks; i++) {
          if (i < totalPage) {
               pagination.append(`<li class="page-item"><a class="page-link" href="#" onclick="displayData(${i}, '${param.job}','${param.skill}')">${i + 1}</a></li>`);
          }
     }

     // Tautan "Next"
     if (currentPage < totalPage - 1) {
          pagination.append(`<li class="page-item"><a class="page-link" href="#" onclick="displayData(${currentPage + 1}, '${param.job}','${param.skill}')">></a></li>`);
     }

     // Tautan "Last Page" (sembunyikan jika di halaman terakhir)
     if (currentPage < totalPage - 1) {
          pagination.append(`<li class="page-item"><a class="page-link" href="#" onclick="displayData(${totalPage - 1}, '${param.job}','${param.skill}')">>></a></li>`);
     }
}

// Fungsi untuk mengupdate informasi pagination
function updatePaginationInfo(currentPage, totalPage, data, param) {
     $("#paginationInfo").text(`Halaman ${currentPage + 1} dari ${totalPage}, Total Data: ${data.pageData.total}`);

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

function detail(id) {
     $("#detailCard").LoadingOverlay("show");
     console.log(id);
     $.ajax({
          url: `/api/talent/${id}`,
          method: "GET",
          success: (result) => {
               let root = $("#detailCard");
               var iframe = $("#talentDetail");
               // var inviteButton = $("#inviteButton");
               iframe.attr("src", `${baseUrl}share/${result.userUrl}`);
               iframe.show();
               root.find(".invite-button").attr("onclick", `invite('${result.id}')`);
               console.log(result);
               // Pemeriksaan status disabled
               // if (result.invited) {
               //   inviteButton
               //     .removeClass("invite-button")
               //     .addClass("invited-button")
               //     .addClass("disabled-button")
               //     .find(".fa-envelope") // Ganti ikon
               //     .removeClass("fa-envelope")
               //     .addClass("fa-envelope-open");
               //   inviteButton.find(".invite-button-text").text("Invited"); // Ganti teks
               // } else {
               //   inviteButton
               //     .removeClass("invited-button")
               //     .removeClass("disabled-button")
               //     .addClass("invite-button")
               //     .find(".fa-envelope-open") // Ganti ikon
               //     .removeClass("fa-envelope-open")
               //     .addClass("fa-envelope");
               //   inviteButton.find(".invite-button-text").text("Invite"); // Ganti teks
               // }

               $("#detailCard").LoadingOverlay("hide", true);
          },
          error: (e) => {
               $("#detailCard").LoadingOverlay("hide", true);
               // inviteButton.show();
               Swal.fire({
                    position: "center",
                    icon: "error",
                    title: "Request gagal dibuat",
                    showConfirmButton: false,
                    timer: 1500,
               });
          },
     });
}

function invite(talentId, recommendationId) {
     var data = JSON.stringify({ id: talentId });
     $.ajax({
          type: "POST",
          url: `/api/talent/encrypt`,
          data: data,
          dataType: "text",
          beforeSend: addCsrfToken(),
          contentType: "application/json",
          success: function (response) {
               console.log(recommendationId);
               window.location.href = `/interview-schedule/create?token=${response}&recommendationId=${recommendationId}`;
          },
     });
}

// Fungsi untuk menampilkan data berdasarkan halaman
function displayData(page, job = "", skill = "", idleStatus) {
     $("#detailCard").LoadingOverlay("show");
     $("#listTalent").LoadingOverlay("show");
     let talentDetailNoData = $("#talentDetailNoData");
     talentDetailNoData.hide();
     let root = $("#detailCard");
     root.LoadingOverlay("show");
     var iframe = $("#talentDetail");
     // penyebabnya
     // var url = `/api/talent/budget?page=${page}&job=${job}&skill=${skill}`;
     // var url = `/api/talent/budget?page=${page}&job=${job}&skill=${skill}&idleStatus=${idleStatus}`;
     var url = `/api/talent/filter?page=${page}&job=${job}&skill=${skill}&size=`;

     if (idleStatus !== null && idleStatus !== undefined && idleStatus !== "") {
          url += `&idleStatus=${idleStatus}`;
     }

     var param = {
          job,
          skill,
     };

     $.ajax({
          type: "GET",
          url: url,
          dataType: "json",
          success: function (response) {
               // console.log(url);
               var data = response;
               var tbody = $("#talentTbl");
               var inviteButton = $("#inviteButton");
               totalPage = data.pageData.total == 10 ? data.pageData.lastPage : data.pageData.lastPage + 1;
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
                    iframe.hide(); // Sembunyikan iframe
                    talentDetailNoData.show();
                    inviteButton.hide();
               } else {
                    $.each(data.data, function (index, talent) {
                         // if(talent.hidden === 0) {
                         // console.log(talent);
                         var skill = `<p class="card-text skills-list">`;
                         var displayedSkills = 0;
                         talent.skill.forEach((element) => {
                              if (displayedSkills < 3) {
                                   var newElement = element.length > 16 ? element.substring(0, 16) + "..." : element;
                                   elementHtml = `<span class="badge mb-2 ml-3 text-white custom-color-2" data-toggle="tooltip" title="${element}">${newElement}</span>`;
                                   skill += elementHtml;
                                   displayedSkills++;
                              }
                         });
                         skill += `</p>`;
                         var row = $("<tr>");
                         let newCell = $("<td>").html(`
                <div class="card rounded-5 talent-list-card my-1 mx-1">
                  <div class="rounded-image object-fit-cover  ml-4">
                    <img src="${talent.photo}" alt="Profile Image"/>
                  </div>
                  <div class="card-body" style="max-width: 60%;">
                    <h6 class="card-title mb-2 ml-3">${talent.name}</h6>
                    <p class="card-text mb-2 ml-3">${talent.currentWorkAssignment ? talent.currentWorkAssignment : "-"}
                    </p>
                    ${skill}

                    
                  </div>
                  <div class="row justify-content-end align-items-center mr-3" style="max-width: 20%;">
                          
                  <a class="btn btn-primary mb-1" onclick="openEditForm('${talent.id}')">                        
                  <i class="bi bi-pencil-square"></i>
                  
                  </a>
                  <a class="btn btn-danger" onclick="deleteForm('${talent.id}')">                        
                  <i class="bi bi-trash"></i>
                      
                  </a>
                  </div>
                </div>              
              `);

                         newCell.find(".talent-list-card").on("click", function (event) {
                              // console.log(talent);
                              detail(talent.id);
                         });

                         row.append(newCell);
                         tbody.append(row);

                         if (index === 0) {
                              iframe.attr("src", `${baseUrl}share/${talent.userUrl}`);
                              root.find(".invite-button").attr("onclick", `invite('${talent.id}')`);
                              iframe.show(); // Tampilkan iframe
                              talentDetailNoData.hide(); // Sembunyikan no data
                         }
                         // }
                    });
               }

               // Sembunyikan loading overlay setelah selesai
               $("#detailCard").LoadingOverlay("hide", true);
               $("#listTalent").LoadingOverlay("hide");
          },
          error: function () {
               // Sembunyikan loading overlay pada kasus error
               $("#detailCard").LoadingOverlay("hide", true);
               $("#listTalent").LoadingOverlay("hide");
               // Handle error here
          },
     });
}

function deleteForm(userId) {
     let username = "53cUReB4n9EtS!h";
     let password = "tO0RS1r4J4P3t!R";

     let credentials = btoa(username + ":" + password);

     // Tampilkan dialog konfirmasi menggunakan SweetAlert
     Swal.fire({
          title: "Apakah Anda yakin?",
          text: "Anda akan menghapus pengguna ini.",
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "Ya, hapus!",
     }).then((result) => {
          if (result.isConfirmed) {
               // Lakukan permintaan AJAX untuk menghapus pengguna
               $.ajax({
                    method: "PUT",
                    url: `${baseUrl}mitra/${userId}`,
                    headers: {
                         Authorization: "Basic " + credentials,
                    },
                    dataType: "json",
                    success: function (response) {
                         Swal.fire("Berhasil!", "Talent telah dihapus.", "success").then((result) => {
                              displayData(currentPage);
                         });
                    },
                    error: function (error) {
                         //  displayData(currentPage);
                         Swal.fire("Berhasil!", "Talent telah dihapus.", "success").then((result) => {
                              displayData(currentPage);
                         });
                    },
               });
          }
     });
}

function openEditForm(talentId) {
     let username = "53cUReB4n9EtS!h";
     let password = "tO0RS1r4J4P3t!R";

     let credentials = btoa(username + ":" + password);
     // Make an AJAX request to get the current details of the talent
     $.ajax({
          method: "GET",
          url: `${baseUrl}mitra/${$("#mitraId").text()}`,
          headers: {
               Authorization: "Basic " + credentials,
          },
          dataType: "json",
          xhrFields: {
               withCredentials: true,
          },
          success: function (response) {
               console.log("Data fetched successfully:", response);
               // console.log(talentId);
               // sessionStorage.setItem("talentId", talentId);

               sessionStorage.setItem("credentials", credentials);

               window.location.href = baseUrl + response.redirect + "?talentId=" + talentId;
          },
          error: function (error) {
               console.error("Error fetching data:", error);
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
displayData(currentPage, "", "", 0);

$(function () {
     $('[data-toggle="tooltip"]').tooltip();
});

function fetchDataWithAuth() {
     let username = "53cUReB4n9EtS!h";
     let password = "tO0RS1r4J4P3t!R";

     let credentials = btoa(username + ":" + password);

     $.ajax({
          method: "GET",
          url: `${baseUrl}mitra/${$("#mitraId").text()}`,
          headers: {
               Authorization: "Basic " + credentials,
          },
          dataType: "json",
          xhrFields: {
               withCredentials: true,
          },
          success: function (response) {
               console.log("Data fetched successfully:", response);

               // Simpan token atau kredensial dalam sessionStorage
               sessionStorage.setItem("credentials", credentials);

               // Arahkan langsung ke halaman /mitra/app
               window.location.href = baseUrl + response.redirect;
          },
          error: function (error) {
               console.error("Error fetching data:", error);
          },
     });
}
