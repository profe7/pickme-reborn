// Function to filter talents by name
function filterTalentsName() {
  let talentName = document.getElementById('searchTalentName').value;
  let clientId = document.getElementById('clientId').value;

  fetchRecommendations(clientId, talentName);
}

// Function to fetch recommendations from the server
function fetchRecommendations(clientId, talentName) {
  $.ajax({
    url: `/client/interview-schedules`,
    type: 'GET',
    data: {
      clientId: clientId,
      search: talentName
    },
    success: function(data) {
      $('#talent-interview-schedules').empty().html($(data).find('#talent-interview-schedules').html());
      $('#pagination-controls').empty().html($(data).find('#pagination-controls').html());
    },
    error: function(error) {
      console.error('Error fetching recommendations:', error);
    }
  });
}

document.addEventListener('DOMContentLoaded', function () {
  const modals = ['rescheduleModal', 'acceptModal', 'rejectModal', 'processModal', 'cancelModal'];

  modals.forEach(modalId => {
    const modal = document.getElementById(modalId);
    modal.addEventListener('show.bs.modal', function (event) {
      const button = event.relatedTarget;
      const interviewId = button.getAttribute('data-id');
      const form = modal.querySelector('form');
      form.action = `/client/update-interview/${interviewId}`;
    });
  });
});
