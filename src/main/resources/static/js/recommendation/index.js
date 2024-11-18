// Function to filter talents by name
function filterTalentsName() {
    let talentName = document.getElementById('searchTalentName').value;
    let position = document.getElementById('searchTalentPosition').value;
    let clientId = document.getElementById('clientId').value;

    fetchRecommendations(clientId, talentName, position);
}

// Function to filter talents by position
function filterTalentsPosition() {
    let talentName = document.getElementById('searchTalentName').value;
    let position = document.getElementById('searchTalentPosition').value;
    let clientId = document.getElementById('clientId').value;

    fetchRecommendations(clientId, talentName, position);
}

// Function to fetch recommendations from the server
function fetchRecommendations(clientId, talentName, position) {
    $.ajax({
        url: `/client/recommendations`,
        type: 'GET',
        data: {
            clientId: clientId,
            talentName: talentName,
            position: position
        },
        success: function(data) {
            $('#talent-recommendations').empty().html($(data).find('#talent-recommendations').html());
            $('#pagination-controls').empty().html($(data).find('#pagination-controls').html());
        },
        error: function(error) {
            console.error('Error fetching recommendations:', error);
        }
    });
}