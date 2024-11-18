// Function to filter talents by name
function filterTalentsName() {
  let talentName = document.getElementById('searchTalentName').value;
  let clientId = document.getElementById('clientId').value;

  fetchTalents(clientId, talentName);
}

// Function to fetch recommendations from the server
function fetchTalents(clientId, talentName) {
  $.ajax({
      url: `/client/interview-schedules`,
      type: 'GET',
      data: {
          clientId: clientId,
          talentName: talentName,
      },
      success: function(data) {
          $('#talent-interview-schedule').empty().html($(data).find('#talent-interview-schedules').html());
          $('#pagination-controls').empty().html($(data).find('#pagination-controls').html());
      },
      error: function(error) {
          console.error('Error fetching talents:', error);
      }
  });


  document.addEventListener("DOMContentLoaded", function () {
    // Ambil elemen-elemen yang dibutuhkan
    const modal = document.getElementById("confirmationModal");
    const modalMessage = document.getElementById("confirmationModalMessage");
    const confirmationForm = document.getElementById("confirmationForm");
    const modalInterviewId = document.getElementById("modalInterviewId");
    const modalAction = document.getElementById("modalAction");

    // Cari semua tombol yang memiliki kelas 'action-button'
    const actionButtons = document.querySelectorAll(".action-button");

    // Tambahkan event listener ke setiap tombol
    actionButtons.forEach(button => {
      button.addEventListener("click", function () {
        // Ambil pesan dari atribut data-message tombol
        const message = this.getAttribute("data-message");

        // Ambil ID dan status dari elemen form terkait tombol
        const form = this.closest("form");
        const interviewId = form.querySelector("input[name='interviewId']").value;
        const action = form.querySelector("input[name='status']").value;

        // Isi modal dengan data yang sesuai
        modalMessage.textContent = message; // Isi pesan modal
        modalInterviewId.value = interviewId; // Isi hidden input ID
        modalAction.value = action; // Isi hidden input status

        // Tampilkan modal
        const bootstrapModal = new bootstrap.Modal(modal);
        bootstrapModal.show();
      });
    });
  });
}
