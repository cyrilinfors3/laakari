'use strict';

describe('Controller Tests', function() {

    describe('Lcarte Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLcarte, MockLpoint, MockLzone;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLcarte = jasmine.createSpy('MockLcarte');
            MockLpoint = jasmine.createSpy('MockLpoint');
            MockLzone = jasmine.createSpy('MockLzone');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Lcarte': MockLcarte,
                'Lpoint': MockLpoint,
                'Lzone': MockLzone
            };
            createController = function() {
                $injector.get('$controller')("LcarteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:lcarteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
