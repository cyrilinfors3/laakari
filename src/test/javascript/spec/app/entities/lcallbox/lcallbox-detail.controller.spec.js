'use strict';

describe('Controller Tests', function() {

    describe('Lcallbox Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLcallbox, MockLvisit, MockLpoint, MockLroute;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLcallbox = jasmine.createSpy('MockLcallbox');
            MockLvisit = jasmine.createSpy('MockLvisit');
            MockLpoint = jasmine.createSpy('MockLpoint');
            MockLroute = jasmine.createSpy('MockLroute');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Lcallbox': MockLcallbox,
                'Lvisit': MockLvisit,
                'Lpoint': MockLpoint,
                'Lroute': MockLroute
            };
            createController = function() {
                $injector.get('$controller')("LcallboxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'laakariApp:lcallboxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
