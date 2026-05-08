import { TestBed } from '@angular/core/testing';

import { AdminSolicitudesService } from './admin-solicitudes';

describe('AdminSolicitudesService', () => {
  let service: AdminSolicitudesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminSolicitudesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
