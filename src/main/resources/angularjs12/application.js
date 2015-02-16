angular.module('ui.bootstrap.demo', ['ui.bootstrap']);
angular.module('ui.bootstrap.demo').controller('MainCtrl',
    function ($scope, $http, $interval) {
        function loadData() {
            $http.get('http://127.0.0.1:8080/service').success(function (data) {
                var json = angular.fromJson(data);
                console.log("load data: " + json);
                $scope.data = json;
            });
        };
        loadData();

        $scope.change = function () {
            var json = angular.toJson($scope.data);
            console.log("change: " + json);
            // TODO add callback handler
            $http.post('/service', json);
        };

        // refresh data from server every 5 seconds
        $interval(function () {
            console.log("refresh data")
            loadData();
        }, 5000);
    });