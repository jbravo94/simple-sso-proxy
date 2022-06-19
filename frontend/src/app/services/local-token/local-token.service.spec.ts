import { TestBed } from '@angular/core/testing';

import { LocalTokenService } from './local-token.service';

describe('LocalTokenService', () => {
  let service: LocalTokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocalTokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
