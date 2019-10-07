import { IPatent } from 'app/shared/model/patent.model';

export interface IClient {
  id?: number;
  uid?: string;
  firstName?: string;
  lastName?: string;
  patents?: IPatent[];
}

export const defaultValue: Readonly<IClient> = {};
