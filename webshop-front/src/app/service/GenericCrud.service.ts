import {HttpClient} from '@angular/common/http';

export class GenericCrudService<REQ, RESP> {
  private readonly path: string;
  protected http: HttpClient;

  constructor(path: string, http: HttpClient) {
    this.path = path;
    this.http = http;
  }

  getAll() {
    return this.http.get<RESP[]>(this.path);
  }

  getById(id: number) {
    const uriId = this.path + `/${id}`;
    return this.http.get<RESP>(uriId);
  }

  create(entity: REQ) {
    return this.http.post<RESP>(this.path, entity);
  }

  update(id: number, entity: REQ) {
    return this.http.put<RESP>(`${this.path}/${id}`, entity);
  }

  delete(id: number) {
    return this.http.delete(`${this.path}/${id}`);
  }
}
