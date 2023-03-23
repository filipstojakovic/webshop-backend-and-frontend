import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export class GenericCrudService<T> {
  private readonly path: string;
  private readonly http: HttpClient;

  constructor(path: string, http: HttpClient) {
    this.path = path;
    this.http = http;
  }

  getAll() {
    return this.http.get<T[]>(this.path);
  }

  getById(id: string) {
    const uriId = this.path + `/${id}`;
    return this.http.get<T>(uriId);
  }

  create(entity: T) {
    return this.http.post<T>(this.path, entity);
  }

  update(id: number, entity: T) {
    return this.http.put<T>(`${this.path}/${id}`, entity);
  }

  delete(id: number) {
    this.http.delete(`${this.path}/${id}`);
  }
}
