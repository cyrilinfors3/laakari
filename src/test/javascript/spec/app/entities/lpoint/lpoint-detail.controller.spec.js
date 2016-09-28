'use strict';

describe('Controller Tests', function() {

    describe('Lpoint Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLpoint, MockLcarte, MockLcallbox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLpoint = jasmine.createSpy('MockLpoint');
            MockLcarte = jasmine.createSpy('MockLcarte');
            MockLcallbox = jasmine.createSpy('MockLcallbox');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Lpoint': MockLpoint,
                'Lcarte': MockLcarte,
                'Lcallbox': MockLcallbox
            };
            createController = function() {
                $injector.get('$controller')("LpointDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:lpointUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
