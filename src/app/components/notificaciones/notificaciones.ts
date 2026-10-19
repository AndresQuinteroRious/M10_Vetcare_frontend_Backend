import { Component, inject } from '@angular/core';
import { NotificacionesService } from '../../services/notificaciones.service';
import { Icono } from '../icono/icono';

@Component({
  selector: 'app-notificaciones',
  imports: [Icono],
  templateUrl: './notificaciones.html',
  styleUrl: './notificaciones.css'
})
export class Notificaciones {
  private notificacionesService = inject(NotificacionesService);

  notificaciones = this.notificacionesService.notificaciones;

  cerrar(id: number) {
    this.notificacionesService.cerrar(id);
  }
}