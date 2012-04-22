var GaeSentapp21 = {
	init: function() {
		this.bindEventListeners();
	},
	
	bindEventListeners: function() {
		var self = this;
		$('#searchsubmit').click(function(event){ self.searchTweets(event); });
	},
	
	searchTweets: function(event) {
		var value = $('#searchterm').val();
		var self = this;
		if (this.validateTerm(value)) {
			$.ajax({
				type: 'POST',
				url: '/tweeanalyzer',
				data: { 'term' : value },
				success: function(data) { 
					var tweets = data['tweets'];
					var parent = $('div.results');
					for (var i = 0; i < tweets.length; i++) {
						if (i % 2) {
							var classs = "odd";
						} else { 
							var classs = "even";
						}
						parent.append($('<span class="tweet"/>').text(tweets[i]));
					}
				},
				dataType: 'json'
			});
		}
	},
	
	validateTerm: function(term) {
		if (term && term != '') {
			return true;
		}
		return false;
	}
};

$(document).ready(function() {
	GaeSentapp21.init();
});