import { IClient } from 'app/shared/model/client.model';

export interface IPatent {
  id?: number;
  uid?: string;
  client?: IClient;
}

export const defaultValue: Readonly<IPatent> = {};
