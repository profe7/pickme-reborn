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


// function cv(id) {
//   $("#container").LoadingOverlay("show");
//   $.ajax({
//     url: `/api/talent/${id}`,
//     method: "GET",
//     success: (result) => {
//       window.open(`https://dev.cv-me.metrodataacademy.id/share/${result.userUrl}`);
//       $("#container").LoadingOverlay("hide", true);
//     },
//     error: (e) => {
//       $("#container").LoadingOverlay("hide", true);
//       Swal.fire({
//         position: "center",
//         icon: "error",
//         title: "Gagal mencari CV",
//         showConfirmButton: false,
//         timer: 1500,
//       });
//     },
//   });
// }

$(document).ready(function() {
  const page = 0
  const size = 10

  var url = `/api/recommendations?page=${page}&size=${size}`; 

  $.ajax({
      url: url,  
      method: 'GET',
      success: function(response) {
          if (response.data.length > 0) {
            console.log(response);
            
              let talentContainer = $('#talent-recommendations');
              talentContainer.empty(); 

              response.data.forEach(function(recommendation) {
                let recommendationDiv = `
                <div class="shadow-sm" style="margin:2rem auto; padding: 1rem 2rem; border: solid 1px rgb(205, 205, 205); border-radius: 10px; width: 1224px;">
                    <h3 class="text-center mt-1" style="margin-bottom: 2rem;">${recommendation.position}</h3>
                    <div class="talent-container" style="width: 1224px; height: 400px; text-align: center; display: flex; flex-direction: row; overflow-x: auto;">
                        <div style="height: 100%; display: flex; flex-direction: row; justify-content: space-between; width: 100%;">
                            ${recommendation.talents.map(talent => `
                                <div class="talent-card" style="height: 100%; width: 200px; margin-bottom: 2rem; text-align: center; display: flex; flex-direction: column; justify-content: space-between;">
                                    <div class="card-body text-center">
                                        <img src="${talent.photo || 'https://cdn-icons-png.flaticon.com/512/3106/3106773.png'}" alt="Profile Image" class="img-box img-fluid" style="width: 150px; height: 200px; object-fit: cover;"/>
                                    </div>
                                    <div class="card-footer text-center" style="margin-bottom: 10px; display: flex; flex-direction: column; justify-content: space-between; height: 120px;">
                                        <h6 style="width: 100%; text-wrap: wrap; margin-top: 1rem;">${talent.name}</h6>
                                        <div>
                                            <button class="btn btn-primary bi bi-envelope mb-2" onclick="invite('${talent.id}', '${recommendation.id}')">Invite</button>
                                            <button class="btn btn-secondary bi bi-envelope-paper mb-2" onclick="cv('${talent.id}')">CV</button>
                                        </div>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                </div>
            `;
            
                  talentContainer.append(recommendationDiv);
              });
          } else {
              $('#talent-recommendations').html('<div>Tidak terdapat rekomendasi talent.</div>');
          }
      },
      error: function() {
          alert('Gagal mengambil data rekomendasi.');
      }
  });
});




function filterTalentsName() {
    var nameInput = $('#searchTalentName').val().toLowerCase();
    filterTalents(nameInput, $('#searchTalentPosition').val().toLowerCase());
  }
  
  function filterTalentsPosition() {
    var positionInput = $('#searchTalentPosition').val().toLowerCase();
    filterTalents($('#searchTalentName').val().toLowerCase(), positionInput);
  }
  
  function filterTalents(name, position) {
    $('.talent-card').each(function() {
      var talentName = $(this).find('h6').text().toLowerCase();
      var talentPosition = $(this).closest('.shadow-sm').find('h3').text().toLowerCase();

      if (talentName.includes(name) && talentPosition.includes(position)) {
        $(this).closest('.shadow-sm').show(); 
      } else {
        $(this).closest('.shadow-sm').hide(); 
      }
    });
  }
  $('#searchTalentName').on('input', filterTalentsName);
  $('#searchTalentPosition').on('input', filterTalentsPosition);
  