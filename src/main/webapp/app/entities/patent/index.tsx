import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Patent from './patent';
import PatentDetail from './patent-detail';
import PatentUpdate from './patent-update';
import PatentDeleteDialog from './patent-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PatentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PatentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PatentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Patent} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PatentDeleteDialog} />
  </>
);

export default Routes;
