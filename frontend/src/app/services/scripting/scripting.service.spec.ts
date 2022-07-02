import { TestBed } from '@angular/core/testing';

import { ScriptingService } from './scripting.service';

describe('ScriptingService', () => {
  let service: ScriptingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScriptingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
