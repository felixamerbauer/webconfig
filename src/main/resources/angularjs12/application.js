angular.module('ui.bootstrap.demo', ['ui.bootstrap']);
angular.module('ui.bootstrap.demo').controller('MainCtrl',
    function ($scope, $http) {
        function loadData() {
            $http.get('http://127.0.0.1:8080/service').success(function (data) {
                console.log("load data: " + JSON.stringify(data));
                json = angular.fromJson(data);
                $scope.data = json;
            });
        };
        loadData();

        $scope.change = function () {
            var updatedData = {
                "singleModel": $scope.data.singleModel,
                "radioModel": $scope.data.radioModel,
                "checkModel": {
                    "left": $scope.data.checkModel.left,
                    "middle": $scope.data.checkModel.middle,
                    "right": $scope.data.checkModel.right
                }
            }
            console.log("change: " + JSON.stringify(updatedData));
            $http.post('service', {
                'key' : 'singleModel',
                'value' : 'asf'
            });
        };
    });