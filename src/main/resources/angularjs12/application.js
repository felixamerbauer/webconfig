angular.module('ui.bootstrap.demo', [ 'ui.bootstrap' ]);
angular.module('ui.bootstrap.demo').controller('ButtonsCtrl',
		function($scope, $http) {
			$scope.singleModel = 1;

			$scope.radioModel = 'Middle';

			$scope.checkModel = {
				left : false,
				middle : true,
				right : false
			};

			$scope.$watch('singleModel', function() {
				$http.post('/service', {
					'key' : 'singleModel',
					'value' : $scope.singleModel + ''
				});
			});
		});