import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Disciplina, CriarDisciplinaRequest } from '../models/disciplina.model';

@Injectable({
  providedIn: 'root'
})
export class DisciplinaService {

  private readonly apiUrl = '/api/disciplinas';

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Disciplina[]> {
    return this.http.get<Disciplina[]>(this.apiUrl);
  }

  criar(request: CriarDisciplinaRequest): Observable<Disciplina> {
    return this.http.post<Disciplina>(this.apiUrl, request);
  }
}