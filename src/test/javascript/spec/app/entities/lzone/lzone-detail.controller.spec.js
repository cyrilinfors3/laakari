'use strict';

describe('Controller Tests', function() {

    describe('Lzone Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLzone, MockLcarte, MockLroute, MockLregion, MockLarrondissement, MockLville, MockLuser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLzone = jasmine.createSpy('MockLzone');
            MockLcarte = jasmine.createSpy('MockLcarte');
            MockLroute = jasmine.createSpy('MockLroute');
            MockLregion = jasmine.createSpy('MockLregion');
            MockLarrondissement = jasmine.createSpy('MockLarrondissement');
            MockLville = jasmine.createSpy('MockLville');
            MockLuser = jasmine.createSpy('MockLuser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Lzone': MockLzone,
                'Lcarte': MockLcarte,
                'Lroute': MockLroute,
                'Lregion': MockLregion,
                'Larrondissement': MockLarrondissement,
                'Lville': MockLville,
                'Luser': MockLuser
            };
            createController = function() {
                $injector.get('$controller')("LzoneDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:lzoneUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
