'use strict';

describe('Controller Tests', function() {

    describe('Lroute Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLroute, MockLcallbox, MockLzone;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLroute = jasmine.createSpy('MockLroute');
            MockLcallbox = jasmine.createSpy('MockLcallbox');
            MockLzone = jasmine.createSpy('MockLzone');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Lroute': MockLroute,
                'Lcallbox': MockLcallbox,
                'Lzone': MockLzone
            };
            createController = function() {
                $injector.get('$controller')("LrouteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:lrouteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
